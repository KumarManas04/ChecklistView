# ChecklistView
## Overview
ChecklistView is an easy to use library to create To-do Lists or Checklists.

## Features
  - Easy to use Custom view
  - Set colors for drag handle, remove item button, text color and hint text color
  - Convert the list to string
  - Input the list content in string format

## Including in your project
Add below codes to your <b>root</b> build.gradle
```
allprojects {
   repositories {
      maven { url 'https://jitpack.io' }
   }
}
```
Add a dependency to your project's <b>module</b> build.gradle
```
dependencies {
     implementation 'com.github.KumarManas04:ChecklistView:1.0'
}
```
## Usage
Add following XML namespace inside your XML layout file.
```
xmlns:app="http://schemas.android.com/apk/res-auto"
```

Add ChecklistView to your layout file
```
<com.infinitysolutions.checklistview.ChecklistView
          android:id="@+id/checklist_view"
          android:layout_width="match_parent"
          android:layout_height="match_parent"/>
```
Optional properties:
```
app:dragHandleColor="@color/dragHandleColor"
app:removeButtonColor="@color/removeButtonColor"
app:textColor="@color/textColor"
app:hintTextColor="@color/hintColor"
```
In your Activity or Fragment get an instance of ChecklistView. Let it be mChecklistView.
```
mChecklistView.setList(itemsList)   // itemsList is an ArrayList of ChecklistItem
         OR
mChecklistView.setList(contentString) //contentString is the Checklist converted into string form
```
To get the Checklist in String form
```
mChecklistView.toString()
```

## Developed by
* Kumar Manas - <kumarmanas04@gmail.com>

## Licence
```
MIT License

Copyright (c) 2019 Kumar Manas

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
