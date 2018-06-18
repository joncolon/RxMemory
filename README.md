## RxMemory

#### * This app is developed using Android Canary Preview Build 3.2 18 *

###### RxMemory is a Kotlin-based Android application to create a classic memory matching card game where users can collect pokemon cards they’ve matched to be viewed in a separate collection screen.

###### Animation Demo: https://youtu.be/EZo7kIWQ2qw

<a href="http://tinypic.com?ref=rrqy2u" target="_blank"><img src="http://i66.tinypic.com/rrqy2u.gif" border="0" alt="Image and video hosting by TinyPic"></a>

- Used RxJava 2 to chain Retrofit calls and Room SQLite Database queries and manage the state of the UI accordingly. All information is stored in Room before being distributed to the view. 
- Structured the app using Model-View-ViewModel architecture using RxJava 2 to pass state and Dagger 2 for dependency injection
- Implemented a custom ItemAnimator to trigger animations upon data changes in the RecyclerView
- Used Data-Binding and custom Binding Adapters to pass photo urls through xml to be processed by Glide. 
