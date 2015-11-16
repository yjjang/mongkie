.. MONGKIE documentation master file, created by
   sphinx-quickstart on Thu Nov  5 19:54:16 2015.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

.. image:: images/logoS_48.png
   :height: 35px
   :width: 35px
   :align: left

*******
MONGKIE
*******

`MONGKIE <http://yjjang.github.io/mongkie>`_ is a software platform for interactive visualization and  analysis of complex omics data in the context of biological networks.

* All components for visualization (e.g. :doc:`Data-to-Visual mapping <network_visualization/visual_editing>` and :doc:`network_analysis/expression_overlay`), network analysis of :doc:`defining subgroups <network_analysis/network_clustering>`, and :doc:`functional interpretation <network_analysis/enrichment_analysis>` of network modules can be easily threaded into a pipeline that allows user interaction at each step.
* It was built on top of the :doc:`implementation/plugin_architecture` to support application extension by third-party developers.

.. important::
    In the :doc:`case_study`, We demonstrate how MONGKIE can be used to identify driver gene candidates and core regulatory modules in the study of TCGA Glioblastoma Multiforme (:ref:`Brennan et al., 2013 <Bren13>`).

.. toctree::
   :maxdepth: 1
   :numbered:
   :caption: Table of Contents
   :name: matertoc

   installation
   tutorial
   case_study
   network_visualization
   network_analysis
   user_interface
   import_and_export
   implementation
   references


.. Indices and tables
.. ==================
.. 
.. * :ref:`genindex`
.. * :ref:`modindex`
.. * :ref:`search`

