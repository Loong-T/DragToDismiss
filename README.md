# DragToDismiss

![Latest release](https://img.shields.io/github/tag/Loong-T/DragToDismiss.svg)

## Usage

Add the JitPack repository to your root build file:
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add recycler-simplification to your dependencies:
```groovy
dependencies {
    implementation 'in.nerd-is:DragToDismiss:1.1.0'
}
```

This library provides a DragToDismissLayout based on FrameLayout and a DragToDismissCoordinatorLayout based on CoordinatorLayout.

### DragToDismissLayout
```xml
<in.nerd_is.dragtodismiss.DragToDismissLayout
  xmlns:android="http://schemas.android.com/apk/res/android"
  xmlns:app="http://schemas.android.com/apk/res-auto"
  android:id="@+id/dragToDismissLayout"
  android:layout_width="match_parent"
  android:layout_height="match_parent"
  app:dtd_dismissDistance="148dp"
  app:dtd_dismissScale="0.75">

    <!-- Your nested scroll view here -->  
</in.nerd_is.dragtodismiss.DragToDismissLayout>
```

Add your listener:
```kotlin
val view = findViewById<View>(R.id.dragToDismissLayout) as HasDismissCallback
view.addListener(DefaultDismissAnimator(this))
```

### CoordinatorLayout
```xml
<in.nerd_is.dragtodismiss.DragToDismissCoordinatorLayout
  xmlns:android="http://schemas.android.com/apk/res/android"
  xmlns:app="http://schemas.android.com/apk/res-auto"
  android:id="@+id/dragToDismissLayout"
  android:layout_width="match_parent"
  android:layout_height="match_parent"
  android:background="@android:color/white">

  <androidx.core.widget.NestedScrollView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:layout_behavior="@string/behavior_drag_to_dismiss"
    app:dtd_dismissDistance="148dp"
    app:dtd_dismissScale="0.75">
    <!-- Content here -->
  </androidx.core.widget.NestedScrollView>
</in.nerd_is.dragtodismiss.DragToDismissCoordinatorLayout>
```

DragToDismissCoordinatorLayout uses a behavior to implement drag to dismiss, so remember to add it in the child view.

Add your listener:
```kotlin
val view = findViewById<View>(R.id.dragToDismissLayout) as HasDismissCallback
view.addListener(DefaultDismissAnimator(this))
```

## License
```
Copyright 2019 Xuqiang ZHENG

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
