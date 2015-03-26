[![Build Status](https://travis-ci.org/fleipold/jgoodies-extensions.svg?branch=master)](https://travis-ci.org/fleipold/jgoodies-extensions)


jgoodies-extensions provides some things I have been writing on top of
Karsten Lentzsch's excellent [JGoodies](http://www.jgoodies.com/)
binding library.

The code quality obviously does not match that of jgoodies.

Features
========

-   MultiSelectionInList to support multiple selections.
-   SelectionInTree
-   CascadingPresentationModel to support notification on hierarchical
    object structure
-   TypeCaseModel to demultiplex a single ValueModel to different
    ValueModels based on the type of the current object.

Some other random stuff.

For documentation look at the examples


Five Minute Tutorial
--------------------

Add the dependency to your pom:

~~~ .xml
<dependency>
          <groupId>org.programmiersportgruppe.jgoodies</groupId>
          <artifactId>jgoodies-extensions</artifactId>
          <version>0.0.1</version>
</dependency>
~~~
