**********
Case Study
**********

High-throughput studies of tumor biology at multiple levels, including genome, transcriptome, and proteome, have been resulting in a greatly increased volume of cancer omics data. Given the huge amount of cancer omics data, it is a major challenge to distinguish driver mutations from passengers, and to reveal functional relationships between them. One powerful approach to the challenge is to analyze data on the context of the biological network. For example, integration of mutation, copy number, and gene expression profiles with a biological interaction network has been proposed as an approach to identify drivers, relying on the assumption that they will cluster on the network ([Bertrand *et al*., 2015]_).

In this section, we demonstrate that how MONGKIE can facilitates the study of structural pattern of altered genes in the study of TCGA GBM data sets () on the `STRING <http://string-db.org/>`_ PPI network to identify candidate driver genes and core gene modules perturbed by them.

Cancer Omics Data
=================

Somatic mutations, DNA copy number alterations, and RNA-seq expressions level 3 data for TCGA GBM cases were obtained from the `UCSC Cancer Browser <https://genome-cancer.ucsc.edu/proj/site/hgHeatmap/#?bookmark=ce15f29a905207cbf3d0dbcdf9d35c18>`_.

An alteration frequency score for each gene was calculated, based on the 273 GBM cases with both somatic mutation and copy number information. Each gene was considered altered if modified by a validated non-synonymous somatic nucleotide substitution, a homozygous deletion, or a multi-copy amplification. These somatic SNVs, indels, and called CNAs are combined to produce the patient-mutation matrix M, where M(i;j) indicates whether the gene i is altered or not in the patient j.

For gene-level expression profiles, we produced the patient-expression matrix G, where G(i;j) represents the expression level, which is a logarithmic scale of upper-quartile normalized RSEM (Li *et al*., 2011) estimats in tumor for the gene i in the patient j.

Extract GBM-altered network
===========================

Network clustering
==================

Results
=======


.. [Bertrand et al., 2015] Bertrand, Denis, *et al*. Patient-specific driver gene prediction and risk assessment through integrated network analysis of cancer omics profiles. *Nucleic acids research* (2015): gku1393. 
