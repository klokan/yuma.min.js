# yuma.min.js

__yuma.min.js__ aims to be a leaner, lighter and more portable user interface for the [YUMA]
(http://github.com/yuma-annotation) annotation system. It's implemented as a 'Google Maps 
JavaScript API'-like library: add annotation features to your existing HTML pages with 
just a few lines of JavaScript!

The present version of yuma.min.js provides annotation for

   * ordinary static images (any JPG, PNG, etc. included in your HTML page)
   * zoomable images embedded with the [Seadragon AJAX] 
     (http://gallery.expression.microsoft.com/SeadragonAjax) JavaScript viewer. 
   * audio files (unfinished, HTML5-capable browsers only)

## Getting Started

To make content in your Web page annotatable, simply perform the following three steps:

__Include the yuma.min.js JavaScript__ file in the \<head\> of your HTML page

     <head>
       ...
       <script type="text/javascript" src="yuma.min.nocache.js"></script>
       ...
     </head>  
     
__Include the yuma.min.js CSS stylesheet__ file in the \<head\> of your HTML page

     <head>
       ...
       <link rel="stylesheet" type="text/css" href="css/yuma.min.css" />
       <script type="text/javascript" src="yuma.min.nocache.js"></script>
       ...
     </head>  
     
__Use the yuma.min.js JavaScript API__ to attach annotation functionality to images on your page.
Note: we recommend doing this through the ``window.onYUMAready`` event handler. This ensures
that all page elements, as well as yuma.min.js itself will have been correctly loaded when you 
start your initialization. 

     <head>
       ...
       <script type="text/javascript">
       window.onYUMAready = function() {
         var annotationLayer = new YUMA.ImageAnnotationLayer('annotateMe');
       }
       </script>
     </head>
     
     <body>
       ...
       <img id="annotateMe" src="myimage.jpg">
       ...
     </body>
     
Calling ``annotationLayer.createNewAnnotation();`` will open the annotation editing form. That's it for the basics. 

## What Else is in There?

Flexible skinning via CSS, customization & I18N of button texts and labels via object literal 
init parameters, lifecycle callbacks so you can get a handle on annotations when they are created, 
edited, deleted etc. etc. Essentially, you'll be able to build your own, customized annotation 
application around yuma.min.js! We'll have a more complete API documentation on the Wiki shortly!

## Developer Info

yuma.min.js is implemented using the [Google Web Toolkit (GWT)](http://code.google.com/webtoolkit/)
and built with [Gradle] (http://www.gradle.org/). Use

``gradle gwtCompile``

to build the project. Your optimized, minified JavaScript will be in the 
/build/gwt/yuma.min folder. Use

``gradle jettyRunWar``

to build the JavaScript and run the project in an embedded Jetty Web server. Example pages
will be available at http://localhost:8080/yuma-min-js/image-example.html (image annotation example), 
http://localhost:8080/yuma-min-js/seadragon-example.html (zoomable image annotation example) and
http://localhost:8080/yuma-min-js/audio-example.html (HTML5 audio example).

For those using Eclipse:

``gradle eclipse``

will generate Eclipse project files. I also added pre-configured launch configurations (named
_image.launch_, _seadragon.launch_ and _audio.launch_) which launch sample pages for image-, 
zoomable-image- and audio-annotation in [GWT development mode]
(http://code.google.com/intl/de-DE/webtoolkit/doc/latest/DevGuideCompilingAndDebugging.html#dev_mode).

## Why Google Web Toolkit?

[GWT](http://code.google.com/webtoolkit/) is a development toolkit which allows developers to
write complex AJAX applications in Java. The GWT cross-compiler translates the client-side part 
of the Java application to optimized JavaScript. (Any server-side parts of the application remain
Java and require a Servlet container like [Apache Tomcat](http://tomcat.apache.org/) to run.)

yuma.min.js is a __purely client-side application__. It __does not__ make any use of GWT's server-side
parts or the [RPC mechanism](http://code.google.com/intl/de-DE/webtoolkit/doc/latest/tutorial/RPC.html).
To us, the reason for choosing GWT was productivity: the speed advantage we gain from using a strongly-typed,
consistently object-oriented language with good tool support simply outweighs the initial 'setup penalty'
that GWT suffers over traditional JavaScript. The fact that GWT automagically creates highly cross-browser
compatible JavaScript is nice, too.  

If you are new to GWT, the recommended development practice is this:

* Start your application in [development mode] 
(http://code.google.com/intl/de-DE/webtoolkit/doc/latest/DevGuideCompilingAndDebugging.html#dev_mode). 
(If you are using Eclipse, just use the provided launchers.)
* While running in development mode, every change you make in the code will be effective immediately - no
need to re-compile, restart the server etc. Refresh your browser to see your changes - just like working 
in regular JavaScript.
* Once you're ready for the real deal, run ``gradle jettyRunWar`` from the command line. This
will compile the application, build the optimized JavaScript, and launch a Jetty Web server with
the sample pages.
