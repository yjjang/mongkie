**************
Implementation
**************

MONGKIE is a desktop application written in Java 1.6+ based on the `NetBeans RCP <https://netbeans.org/features/platform/index.html>`_ (Rich Client Platform), thus it is executable on all major operating systems such as Windows, Linux, or Mac, and provides robust ways to extend functionalities of the application with ease. In this section, we first describe the main software design and plug-in architecture focusing on its robustness and extensibility, then the multi-tiered system powered by RESTFul Web Service APIs for abstracting data and separating them from the business logics and presentation layers.

.. toctree::
    :caption: Table of Contents
    
    implementation/plugin_architecture
    implementation/restful_webservice
