# Flexible checklist

This is a general checklist application. Checklist itself can be imported from any external file or just created/edited in the application. One checklist (Cessna172, Hebrew version) is already included in application - if you don't need it just delete it in a main screen.

## Checklists editing
Use "Edit" icon in right of a checklist name in order to edit it

## Checklists creation
Use "Menu" -> "Create new checklist" and specify name (this is only a filename, title you can specify in a checklist itself). The empty template for a checklist will be created. Just edit it using edit button

## Checklists import
You can prepare file in advance with you checklist and then download it on your device and import to the application. Use "Menu" -> "Import checklist from file" for import checklist from the file. The file format must be:
```
***** Title ****
  Topic 1
Item 1
Item 2
...
Item n

  Topic 2
Item 1
Item 2
...
Item n
```

Note that checklist may be imported from an URL as well ("Menu" -> "Import checklist from URL")

## Checklist export
If you edited your checklist and wish to save oit to the future it may be exported to any file using "Menu" -> "Export checklist to file".

## Thanks to:

*   Follow files:
**    FileListFragment.java
**    FileLoader.java
**    FileChooserActivity.java
**    FileUtils.java
**    LocalStorageProvider.java
**    FileListAdapter.java
**    open_file_dialog_item_layout.xml
are borrowed from the https://github.com/iPaulPro/aFileChooser.git project. See
    LICENSE-aFileChooser.txt
    README-aFileChooser.md
    LICENSE-IANLAKE.txt
for details

* AutoResizeTextView.java based on this thread in stackoverflow.com
https://stackoverflow.com/questions/5033012/auto-scale-textview-text-to-fit-within-bounds
