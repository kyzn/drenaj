.. Direnaj documentation master file, created by
   sphinx-quickstart on Fri Sep 13 14:33:58 2013.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

Welcome to Direnaj's documentation!
===================================

The documentation for direnaj is largely automatically created directly from
our Python source files.

The process utilizes a convention called `Python docstrings`. If you check
these two links:

http://pythonhosted.org/an_example_pypi_project/sphinx.html#full-code-example

http://pythonhosted.org/an_example_pypi_project/pkgcode.html

you'll see that it's fairly easy to write them as you begin a new function.

As of 13 September 2013, I included all the modules in the documentation. But
if you need to add new modules, just run the following in `./docs`.

>>> python generate_modules.py -s rst -d modules/ ../direnaj/
>>> make html

We'll develop as we go on.

.. autosummary::
   :toctree:

   ../direnaj/config

Modules
===============

.. toctree::
   :maxdepth: 2
   :glob:

   modules/direnaj_collection_templates
   modules/*


Indices and tables
==================

* :ref:`genindex`
* :ref:`modindex`
* :ref:`search`

