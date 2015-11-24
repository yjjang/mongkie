*******************
Interaction Sources
*******************

Systems biology aims to study the relationships between molecular or functional components to gain insights into the underlying complexity and dynamics of biological processes from the properties or structure of the interactions (:ref:`Kitano et al., 2002 <Kita02>`). This generally requires interaction data from diverse sources - e.g. protein-protein interactions, signaling or metabolic pathways - to be integrated, and analyzed together with data derived from high-throughput experiments, such as genomics, transcriptomics, and proteomics. One example of this integrative approach is to analyze altered genes in specific disease samples on the context of biological networks. By projecting the list of mutated, amplified, or deleted genes onto biological networks, one will find statistically significant subsets of related genes that are closely clustered as network modules or biological pathways affected by such genes.

Network-based multi-omics analyses can thus provide important insights into complex biological mechanisms and processes - e.g. the biology underlying disease etiology, or progression of several cancer types (:ref:`Jones et al., 2008 <Jone08>`) - and reliable pathway databases as well as high-coverage protein interaction dataset are essential for such an analysis. MONGKIE is integrated with hiPathDB (:ref:`Yu et al., 2012 <YuSe12>`) which is the Human Integrated Pathway Database described below. Furthermore, it provides an interaction manager that allows users to incorporate external interaction data from multiple sources into the process of network analysis and visualization, and to manage them through an integrated UI.

.. toctree::
    :caption: Table of Contents
    
    interaction_sources/hipathdb
    interaction_sources/interaction_manager
