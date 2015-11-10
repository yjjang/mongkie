## Load somatic mutation data
somatic.muts <- read.table('DATA/TCGA_GBM_mutation_broad_gene-2015-02-24/genomicMatrix',
                           header = T, comment.char = '', stringsAsFactors = F, na.strings = '')
colnames(somatic.muts)[1] <- 'Gene.Symbol'

## Load CNV data
cnv <- read.table('DATA/TCGA_GBM_gistic2thd-2015-02-24/genomicMatrix',
                  sep = '\t', header = T, comment.char = '', stringsAsFactors = F, na.strings = '')
cnv[, 'Gene.Symbol'] <- sapply(strsplit(cnv[, 'Gene.Symbol'], '[|]'), '[', 1)

## Load expression data
rna.seq <- read.table('DATA/TCGA_GBM_exp_HiSeqV2-2015-02-24/genomicMatrix', header = T, stringsAsFactors = F)
colnames(rna.seq)[1] <- 'Gene.Symbol'

## Load clinical data
clinical.dat <- read.table('DATA/TCGA_GBM_exp_HiSeqV2-2015-02-24/clinical_data',
                           sep = '\t', na.strings = "", header = T, as.is = "sampleID")
# Solid Tissue Normal (sample code = 11)
samples.normal <- intersect(colnames(rna.seq),
                            gsub('-', '.', with(clinical.dat, sampleID[which(sample_type_id == 11)])))

# Samples having both mutation and CNV data
samples.altered <- colnames(somatic.muts)[c(F, colnames(somatic.muts)[2:ncol(somatic.muts)] %in% colnames(cnv))]
stopifnot(all(samples.altered %in% colnames(somatic.muts)),
          all(samples.altered %in% colnames(cnv)))

## somatic mutation freq. profile
somatic.muts.altered <- somatic.muts[, c('Gene.Symbol', samples.altered)]
somatic.muts.altered <- subset(somatic.muts.altered, Gene.Symbol != 'Unknown')
rownames(somatic.muts.altered) <- 1:nrow(somatic.muts.altered)
somatic.muts.altered$som.freq <- rowSums(somatic.muts.altered[, samples.altered])
somatic.muts.altered <- somatic.muts.altered[, c('Gene.Symbol', 'som.freq')]
head(somatic.muts.altered[order(-somatic.muts.altered$som.freq), ], n = 20)
sum(somatic.muts.altered$som.freq > 6)
rm(somatic.muts)

## CNV freq. profile
cnv.altered <- cnv[, c('Gene.Symbol', samples.altered)]
cnv.altered$amp.freq <- apply(cnv[, samples.altered], 1, function(c) sum(c == 2))
cnv.altered$del.freq <- apply(cnv[, samples.altered], 1, function(c) sum(c == -2))
cnv.altered <- cnv.altered[, c('Gene.Symbol', 'amp.freq', 'del.freq')]
head(cnv.altered[order(-cnv.altered$amp.freq), ], n = 20)
head(cnv.altered[order(-cnv.altered$del.freq), ], n = 20)
sum(cnv.altered$amp.freq > 9 | cnv.altered$del.freq > 9)
rm(cnv)

## Alteration profile: freq. of somatic mutations + CNVs
mut.profile <- data.frame(Gene.Symbol = somatic.muts.altered[, 'Gene.Symbol'])
mut.profile <- merge(mut.profile, somatic.muts.altered, by = 'Gene.Symbol', all.x = T)
mut.profile <- merge(mut.profile, cnv.altered, by = 'Gene.Symbol', all = T)
mut.profile <- within(mut.profile, {
  som.freq[is.na(som.freq)] <- 0
  amp.freq[is.na(amp.freq)] <- 0
  del.freq[is.na(del.freq)] <- 0
})
head(mut.profile[order(-mut.profile$som.freq), ], n = 20)
head(mut.profile[order(-mut.profile$amp.freq), ], n = 20)
head(mut.profile[order(-mut.profile$del.freq), ], n = 20)
# Freq. threshold
mut.profile <- subset(mut.profile, som.freq > 6 | amp.freq > 9 | del.freq > 9)
rownames(mut.profile) <- 1:nrow(mut.profile)

## Expression profile
# Altered samples having expression data
samples.altered.exp <- intersect(colnames(rna.seq), samples.altered)
rna.seq.altered <- rna.seq[, c('Gene.Symbol', samples.altered.exp)]
# mean exp. over tumor samples
rna.seq.altered$exp.means <- rowMeans(rna.seq.altered[, samples.altered.exp])
rownames(rna.seq.altered) <- rna.seq.altered$Gene.Symbol
# mean exp. over normal samples
rna.seq.altered$exp.means.normal <- rowMeans(rna.seq[, samples.normal])
# exp. log2FC (tumor mean - normal mean)
rna.seq.altered$exp.log2fc <- rna.seq.altered$exp.means - rna.seq.altered$exp.means.normal
# Expression subtypes
table(clinical.dat$GeneExp_Subtype)
samples.subtype.Classical <- intersect(samples.altered.exp,
                                       gsub('-', '.', with(clinical.dat, sampleID[which(GeneExp_Subtype == 'Classical')])))
samples.subtype.Mesenchymal <- intersect(samples.altered.exp,
                                       gsub('-', '.', with(clinical.dat, sampleID[which(GeneExp_Subtype == 'Mesenchymal')])))
samples.subtype.Neural <- intersect(samples.altered.exp,
                                       gsub('-', '.', with(clinical.dat, sampleID[which(GeneExp_Subtype == 'Neural')])))
samples.subtype.Proneural <- intersect(samples.altered.exp,
                                       gsub('-', '.', with(clinical.dat, sampleID[which(GeneExp_Subtype == 'Proneural')])))
rm(clinical.dat)
rm(rna.seq)

library(STRINGdb)
string.db <- STRINGdb$new(version = "10", species = 9606, input_directory = 'DB/10')

## map Hugo Symbol to STRING id
p.alias <- read.table('DB/10/9606__protein_aliases.tsv',
                      header = T, sep = '\t', stringsAsFactors = F, comment.char = '', quote = '')
p.alias.hugo <- subset(p.alias, source == 'BioMart_HUGO')
rm(p.alias)
muts.mapped <- merge(mut.profile, p.alias.hugo, by.x = 'Gene.Symbol', by.y = 'alias')
colnames(muts.mapped)[6] <- 'STRING_id'
#muts.mapped <- subset(muts.mapped, !duplicated(STRING_id))
rownames(muts.mapped) <- muts.mapped$STRING_id

## Altered genes having mapped protein ids in STRINGdb
genes.altered <- muts.mapped$STRING_id

library(igraph)

## Build a STRINGdb network filtered by combined_score cut-off
G <- string.db$get_graph()
# combined_score cutoff
STRING.CUTOFF <- 900
g <- subgraph.edges(G, which(E(G)$combined_score > STRING.CUTOFF))
rm(G)
rm(string.db)

## Find all shortest paths between altered genes in STRINGdb network
vertices.altered.all <- which(V(g)$name %in% genes.altered)
shortest.paths.all <- lapply(vertices.altered.all, function(from) {
  shortest.paths <- all_shortest_paths(g, from, vertices.altered.all)$res
  unlist(shortest.paths[sapply(shortest.paths, function(p) {
    length(p) > 1 & length(p) < 4 # shortest path threshold = 2
    })])
})
vertices.for.subnet <- unique(unlist(shortest.paths.all))
# number of altered genes will be in the extracted sub-network
sum(sapply(shortest.paths.all, length) > 1)
# or vertices of those genes
vertices.altered <- vertices.altered.all[sapply(shortest.paths.all, length) > 1]
vertices.linker <- setdiff(vertices.for.subnet, vertices.altered)
rm(shortest.paths.all)

## Extract GBM alterations-specific sub-network
g.sub <- induced_subgraph(g, vertices.for.subnet)
g.sub

## Retain significant linkers only
names.altered <- V(g)$name[vertices.altered]
names.linker <- V(g)$name[vertices.linker]
linker.p.values <- sapply(names.linker, function(lname) {
  linker <- V(g.sub)[lname]
  # (1) success in sample = edge num of the linker connected to input genes in g.sub
  succ.in.sample <- sum(V(g.sub)$name[unique(neighbors(g.sub, linker))] %in% names.altered)
  # (2) number of success in background = global degree of the linker in g
  succ.in.back <- length(unique(neighbors(g, V(g)[lname])))
  # (3) population size (total num of genes in g) - (2)
  fail.in.back <- vcount(g) - succ.in.back
  # (4) sample size (number of trials) = num of input genes in g.sub
  sample.size <- length(names.altered)
  # Enrichment test using hypergeometric distribution.
  phyper(succ.in.sample - 1, succ.in.back, fail.in.back, sample.size, lower.tail = F)
})
linker.p.adjusted <- p.adjust(linker.p.values, method = 'fdr')
sum(linker.p.adjusted <= 0.01)
names.linker.selected <- names.linker[linker.p.adjusted <= 0.01]

## Final network with altered genes + significant linkers only
g.sub.final <- induced_subgraph(g.sub, V(g.sub)[c(names.altered, names.linker.selected)])
g.sub.final <- delete_vertices(g.sub.final, V(g.sub.final)[degree(g.sub.final) < 1])
g.sub.final

#### Vertices ####
vertices.result <- as_data_frame(g.sub.final, what = "vertices")
# Add hugo symbol
vertices.result <- merge(vertices.result, p.alias.hugo[, c('protein_id', 'alias')],
                        by.x = 'name', by.y = 'protein_id', all.x = T)
colnames(vertices.result)[2] <- 'hgnc_symbol'
sum(is.na(vertices.result$hgnc_symbol))
# Add alteration freq.
vertices.result <- merge(vertices.result,
                         muts.mapped[, c("STRING_id", "som.freq", "amp.freq", "del.freq")],
                         by.x = 'name', by.y = 'STRING_id', all.x = T)
vertices.result[is.na(vertices.result$hgnc_symbol), ]
# NA to 0
vertices.result <- within(vertices.result, {
  som.freq[is.na(som.freq)] <- 0
  amp.freq[is.na(amp.freq)] <- 0
  del.freq[is.na(del.freq)] <- 0
})
# Total alteration freq.
vertices.result[, 'Freq'] <- rowSums(vertices.result[, c('som.freq', 'amp.freq', 'del.freq')])
# Altered or linker
vertices.result <-
  cbind(vertices.result,
        ifelse(vertices.result$name %in% names.altered, 'ALTERED', 'LINKER'))
colnames(vertices.result)[7] <- 'Type'
sum(vertices.result$Type == 'ALTERED')
sum(vertices.result$Type == 'LINKER')
stopifnot(all(with(vertices.result, Freq[Type == 'ALTERED'] > 0)),
          all(with(vertices.result, Freq[Type == 'LINKER'] == 0)))
head(vertices.result[order(-vertices.result$Freq), ], n = 20)
#vertices.result <- merge(vertices.result,
#                         rna.seq.altered[, c('Gene.Symbol', 'exp.means', 'exp.means.normal')],
#                         by.x = 'hgnc_symbol', by.y = 'Gene.Symbol',
#                         all.x = T)
#sum(is.na(vertices.result$exp.means) | is.na(vertices.result$exp.means.normal))
#vertices.result <- subset(vertices.result, !is.na(exp.means) & !is.na(exp.means.normal))
## Write vertices to a CSV file
rownames(vertices.result) <- vertices.result$name
write.csv(vertices.result,
          paste('DATA/tcga_gbm_vertices_6.9_', '.csv', sep = toString(STRING.CUTOFF)),
          row.names = F)


## Exp. attributes
exp.tumor.vs.norm <- rna.seq.altered[, c('Gene.Symbol', 'exp.means', 'exp.means.normal')]
colnames(exp.tumor.vs.norm) <- c('Gene.Symbol', 'Tumor', 'Normal')
write.csv(exp.tumor.vs.norm, 'DATA/tcga_gbm_exp_means_6.9.csv', row.names = F)
exp.log2fc <- rna.seq.altered[, c('Gene.Symbol', 'exp.log2fc')]
colnames(exp.log2fc) <- c('Gene.Symbol', 'log2FC')
write.csv(exp.log2fc, 'DATA/tcga_gbm_exp_log2fc_6.9.csv', row.names = F)
# Vertices gene symbols having exp. data
vertices.symbol.exp <- intersect(rna.seq.altered$Gene.Symbol, vertices.result$hgnc_symbol)
exp.means.subtypes <- data.frame(Gene.Symbol = vertices.symbol.exp,
                                 Classical = rowMeans(rna.seq.altered[vertices.symbol.exp, samples.subtype.Classical]),
                                 Mesenchymal = rowMeans(rna.seq.altered[vertices.symbol.exp, samples.subtype.Mesenchymal]),
                                 Neural = rowMeans(rna.seq.altered[vertices.symbol.exp, samples.subtype.Neural]),
                                 Proneural = rowMeans(rna.seq.altered[vertices.symbol.exp, samples.subtype.Proneural]))
write.csv(exp.means.subtypes, 'DATA/tcga_gbm_exp_subtypes_6.9.csv', row.names = F)


#### Edges ####
edges.result <- as_data_frame(g.sub.final, what = "edges")
## Edge weights: expression correaltions in tumor samples
rna.seq.altered.t <- t(rna.seq.altered[, samples.altered.exp])
edges.result$i.weight <- apply(edges.result[, c('from', 'to')], 1, function(x) {
  from <- vertices.result[x[1], 'hgnc_symbol']
  to   <- vertices.result[x[2], 'hgnc_symbol']
  if(from %in% colnames(rna.seq.altered.t) & to %in% colnames(rna.seq.altered.t)) {
    abs(cor(rna.seq.altered.t[, from], rna.seq.altered.t[, to], method = 'pearson'))
  } else {
    .0
  }
})
# NA correlation to 0
sum(is.na(edges.result$i.weight))
edges.result$i.weight[is.na(edges.result$i.weight)] <- .0
## Write edges to a CSV file
write.csv(edges.result,
          paste('DATA/tcga_gbm_edges_6.9_', '.csv', sep = toString(STRING.CUTOFF)),
          row.names = F)
