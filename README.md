MultiTiDeTree
=============

This is a BEAST 2 package that combines two popular models in research,
MultiTypeTree and TiDeTree. The premise of the model is based on MultiTypeTree,
but with the likelihood computation model deriving from TiDeTree.

The purpose for which this package was developed is to infer tumor phylogenies from
single-cell sequencing data. Historically, tumor phylogenies have been reconstructed
using methods depending on single-cell lineage recorder data. These methods often rely
on an enzyme, such as CRISPR-Cas9, to infer these relationships by seeing which target 
regions of the genome are edited or scarred and passed on to successive generations.

TiDeTree is a framework that specifically works well for this task, using a substitution model 
based on a lineage recorder with target genomic regions that can be scarred.

Integrating MultiTypeTree with the TiDeTree substitution model allows us to use MultiTypeTree's
sub-population models and migration rates with the lineage recorder, allowing us to take into 
account which part of the body each tumor cell was found in and documenting its spread through
the tree.

This package was derived from the two aforementioned models:

https://github.com/tgvaughan/MultiTypeTree

https://github.com/seidels/tidetree
