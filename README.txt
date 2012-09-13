You will need to have Java (JDK or JRE) 1.5 or above to execute this program.

To start the program, just double-click on the MobiMetaEditorV0.16.jar file.

If that doesn't work, type this from the command-line:
	java -jar MobiMetaEditorV0.16.jar

This was written based on the MOBI file format describe in:
http://wiki.mobileread.com/wiki/MOBI

This application is licensed under the MIT License (http://www.opensource.org/licenses/mit-license.php).


ChangeLog

v0.16
- rewrote the language codes routines

v0.15
- added the ability to add/edit the TTS EXTH record
- fixed issues with ConcurrentModificationExceptions thrown
- added more language codes

v0.14
- does not pack the header if the full name field and EXTH records remain unchanged
- added Open and Save menu items
- lets the user specify the target filename

v0.13
- added a Header Info dialog, which details various fields in the PDB header, PalmDOC header, and MOBI header
- added in provisions to invoke debug and safe modes from the command line
- added in WhisperPrep, which lets users set ASINs and sets the CDEType to EBOK
  (to invoke, type "java -cp MobiMetaEditorV0.16.jar cli.WhisperPrep
  <input directory> <output directory>")

v0.12
- changed the GUI to use FileDialog instead of JFileChooser for a more native look and feel
- added support for window modified indicator on OS X
- lets the user specify an input filename on the command line

v0.11
- fixed some MobiHeader size calculation bugs
- added facilities to edit the language fields

v0.10
- initial release
