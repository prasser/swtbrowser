A simple browser widget for SWT 
====

This project provides a simple browser widget for SWT/JFace. SWT already offers the best integration with a system's native
browser of all GUI toolkits for Java. Via the widget `org.eclipse.swt.browser.Browser` it is possible to embed 
IE on Windows, Firefox on Linux and Safari on Mac.

So why yet another (highly limited) browser widget?

While being as powerful as a modern browser should be, SWT's browser widget has two drawbacks. Firstly, it often loads quite
slowly, because it needs to start a fully fledged modern browser. Secondly, I experienced quite a lot of problems on Linux
systems, where environment variables may need to be set up correctly or JVM parameters may need to be specified to enable SWT
to successfully launch the default browser of the current desktop environment.

Being in need of a quick solution to these problems and only wanting to display simple HTML pages, I looked around for alternative
browser widgets for Java. There are quite a few solutions for the Swing toolkit, but using them was not an option, as it is not 
possible to embed Swing or AWT components into SWT applications while delivering a good user experience. As a consequence, I 
developed this browser widget, which is able to display simple HTML pages in SWT.

Features
------

1. API is a subset of the API provided by `org.eclipse.swt.browser.Browser`
2. The widget supports the following HTML tags
	- Anchors: `<a>` (hyperlinks only)
	- Paragraphs: `<p>`
	- Lists: `<li>`, `<ol>`, `<ul>` 
	- Headings: `<h1>`, `<h2>`, `<h3>`, `<h4>`
	- Images: `<img>`
	- Linebreaks: `<br>`
	- Text styles: `<i>`, `<u>`, `<del>`, `<em>`, `<strong>`, `<strike>`
3. Asynchronous loading of HTML documents and images
4. Caching of HTML documents and images
5. Supports HTTPS

Details
------

The widget does not render a webpage onto a canvas, but instead renders it into a set of native widgets/controls/components. As a
consequence, large websites may take some time to load as the widget consumes a lot of system resources in these cases.
To spice things up and have the browser provide something "special", hyperlinks are not embedded into the text as usual. Instead,
the links within each container are collected and a combo box (or a button), which allows navigation, is generated after each 
block of text.

Example
------	

The following code creates a shell and displays the Wikipedia page about Web Browsers:

```Java
// Create display and shell
Display display = new Display();
Shell shell = new Shell(display);
        
// Create browser
HTMLBrowser browser = new HTMLBrowser(shell, SWT.BORDER);
browser.setUrl("http://en.wikipedia.org/wiki/Web_browser");
```

The following screenshot shows the result:

[![Screenshot](https://raw.github.com/prasser/swtbrowser/master/media/screenshot.png)](https://raw.github.com/prasser/swtbrowser/master/media/screenshot.png)

Download
------
A binary version (JAR file) is available for download [here](https://rawgithub.com/prasser/swtbrowser/master/jars/swtsimplebrowser-0.0.1.jar).

The according Javadoc is available for download [here](https://rawgithub.com/prasser/swtbrowser/master/jars/swtsimplebrowser-0.0.1-doc.jar). 

Dependencies
------

SWT Simple Browser depends on SWT (obviously) and tagsoup (for parsing HTML). The versions used to build the binaries provided on
this page can be found [here](https://github.com/prasser/swtbrowser/tree/master/lib).

Documentation
------
Online documentation can be found [here](https://rawgithub.com/prasser/swtbrowser/master/doc/index.html).

License
------
EPL 1.0
