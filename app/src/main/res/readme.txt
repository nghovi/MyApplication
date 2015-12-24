api key: AIzaSyCwisjqRpjJaqQeDKMrRoW91g4ub593QWQ
alias is selfnote, password is y...



//Exp
Now i understannd why we shouldn't use match_parent, esp in item view. It can overlap other view!
shift ctrl alt j
one way to cancel alarm: http://stackoverflow.com/questions/4315611/android-get-all-pendingintents-set-with-alarmmanager

-1. automatic model from json
-2. find checked icon
3.show loading dialog, or there will be exception when quick change from task list -> other
-4.create interface for each request, do not use onAPI...
-5.auto model from server, like get by "Key". review previous project. (at the second thought, it's better create model, so easy for call on client.
6.custom edit text, increase minLines when tap enter,... see Message of Quicker
-7. delete all Delimiter character for Book
8. Build must-read notice, show shortcut, vv...
9. create custom layout for foldable linear layout
-10. Add edit icon at the header of book_detail, remove all add icon at this page, create new edit page, using dialog with edt
-11. fold/unfold word
12. Add star mark for word, to remember it, using preference, maybe
-13. Fold/unfold header section at book detail fragment, since some book has a great list of words
-14. search in book and task
15.http://www.tutorialspoint.com/android/android_text_to_speech.htm
16. Fix bug: request of TakListFragment return result after we switched to book list. So we should cancel all request before entering new fragment, but not background request
17. Delete all unused resource
-18. Using gcm http://www.androidhive.info/2012/10/android-push-notifications-using-google-cloud-messaging-gcm-php-and-mysql/
-19. Put edit cursor when tap on unfold button
-20. Fix search with author name
-21. test case: network error, first time user, search function
-22. if there isn't table task yet, then load and save all tasks, books
-23. test case: update search result after edit task from search result then back
-24. Book search function, let's do as task
-26. http://stackoverflow.com/questions/18634207/difference-between-add-replace-and-addtobackstack
-27. Stop show mottos if MainActivity onPause( tap home button)
-27. Stop, cancel alarm after delete Notice
 28. Instead of changing language, try something like: receive a phone call, turn off device screen, start another activity
 29. Add book name for phrases
 30. Mark system for words that hard to remember
 31. bug: when editting book, note,... tap other icon footer.






An app for noting, tasking, and learning second language.


We usually struggle to choose among tons of reach-features, note-like app.
But I doubt that you ever tried all features of those.
So you may need the really simple, easy-to-use but fast and convenience app.
And here my app comes.
Basically, we used this app to:
- Write down our brilliant idea
- Manage our daily task effectively so we can be more productive
- Build our own vocabulary so we learn second language faster

Right now, you don't need to have a network connection to use this app.
Your data will not be saved to remote server. This secure your important data.

Future, I will focus on improving above features, making them more useful, smart
New features may come but only if it follows:
- our rule: simple but powerful
- our goal: be productive and happy





























 so, transition.replace will remove ALL fragment attached to same containerID (= remove, then add) but not call onDestroy, onDetach, just called onDestroyView
 but, when back button, will call onPause,onStop,onDestroy,then new Fragament onCreateView->onResume, then old fragment will onDestroy, onDetach.

 when popBackStackInclusive, only top fragment call onPause, onStop, onDestroyView, other will call onDestroy, onDetach right away.

* BookList -> BookAdd, then back
 bookadd onPause, onStop, onDestroyView -> bookList onCreateView, onActivityCreated, onStart, onResume -> now bookAdd onDestroy, onDetach.

* Tap home button, then back to app:
1. when tap home button, only fragment on top will call onPause even before activity onPause, then onStop before activity onStop (not onDestroyView as back to previous fragmeent)
2. when tap back to app, activity will call onRestart->onStart-> fragment onStart -> activity onResume -> fragment onResume

* In case of change language: activity A, fragment stack: 1. F1, 2. F2, 3.F3 (f3 on stop):
1. tab home button 1st time: same as above case
2. tab app icon after changing language:
activity (none)F2.onDestroy->onDetach->F3.onDestroy->onDetach->F1.onDestroy->onDetach ->
A.onDestroy -> A.onCreate (now fragment stack is still 1.F1, 2.F2, 3.F3) F2.onAttach->onCreate->F1.onAttack->onCreate->F3.onAttach->onCreate->onCreateView->...error


11-30 03:05:32.466 22699-22699/com.example.vietnguyen.myapplication E/******: 61 Fragment onDestroy: class com.example.vietnguyen.controllers.Task.TaskDetailFragment
11-30 03:05:32.466 22699-22699/com.example.vietnguyen.myapplication E/******: 62 Fragment onDetach: class com.example.vietnguyen.controllers.Task.TaskDetailFragment
11-30 03:05:32.466 22699-22699/com.example.vietnguyen.myapplication E/******: 63 Fragment onDestroy: class com.example.vietnguyen.controllers.Task.TaskListFragment
11-30 03:05:32.466 22699-22699/com.example.vietnguyen.myapplication E/******: 64 Fragment onDetach: class com.example.vietnguyen.controllers.Task.TaskListFragment
11-30 03:05:32.466 22699-22699/com.example.vietnguyen.myapplication E/******: 65 Fragment onDestroyView: class com.example.vietnguyen.controllers.Task.TaskEditFragment
11-30 03:05:32.466 22699-22699/com.example.vietnguyen.myapplication E/******: 66 Fragment onDestroy: class com.example.vietnguyen.controllers.Task.TaskEditFragment
11-30 03:05:32.466 22699-22699/com.example.vietnguyen.myapplication E/******: 67 Fragment onDetach: class com.example.vietnguyen.controllers.Task.TaskEditFragment
11-30 03:05:32.466 22699-22699/com.example.vietnguyen.myapplication E/******: 68 onDestroy com.example.vietnguyen.controllers.MainActivity
11-30 03:05:32.470 22699-22699/com.example.vietnguyen.myapplication E/******: 69 onCreate com.example.vietnguyen.controllers.MainActivity
11-30 03:05:32.474 22699-22699/com.example.vietnguyen.myapplication E/******: 70 Fragment onAttach: class com.example.vietnguyen.controllers.Task.TaskDetailFragment
11-30 03:05:32.474 22699-22699/com.example.vietnguyen.myapplication E/******: 71 Fragment onCreate: class com.example.vietnguyen.controllers.Task.TaskDetailFragment
11-30 03:05:32.474 22699-22699/com.example.vietnguyen.myapplication E/******: 72 Fragment onAttach: class com.example.vietnguyen.controllers.Task.TaskListFragment
11-30 03:05:32.474 22699-22699/com.example.vietnguyen.myapplication E/******: 73 Fragment onCreate: class com.example.vietnguyen.controllers.Task.TaskListFragment
11-30 03:05:32.474 22699-22699/com.example.vietnguyen.myapplication E/******: 74 Fragment onAttach: class com.example.vietnguyen.controllers.Task.TaskEditFragment
11-30 03:05:32.474 22699-22699/com.example.vietnguyen.myapplication E/******: 75 Fragment onCreate: class com.example.vietnguyen.controllers.Task.TaskEditFragment
11-30 03:05:32.482 22699-22699/com.example.vietnguyen.myapplication E/******: 76 Fragment onActivityCreated: class com.example.vietnguyen.controllers.Task.TaskEditFragment
11-30 03:05:32.482 22699-22699/com.example.vietnguyen.myapplication E/******: 77 Fragment buildLayout: class com.example.vietnguyen.controllers.Task.TaskEditFragment
11-30 03:05:32.486 22699-22699/com.example.vietnguyen.myapplication E/******: 78 Fragment onDestroyView: class com.example.vietnguyen.controllers.Task.TaskEditFragment
11-30 03:05:32.490 22699-22699/com.example.vietnguyen.myapplication E/******: 79 Fragment onActivityCreated: class com.example.vietnguyen.controllers.Task.TaskDetailFragment
11-30 03:05:32.490 22699-22699/com.example.vietnguyen.myapplication E/******: 80 Fragment buildLayout: class com.example.vietnguyen.controllers.Task.TaskDetailFragment
11-30 03:05:32.506 22699-22699/com.example.vietnguyen.myapplication E/AndroidRuntime: FATAL EXCEPTION: main
11-30 03:05:32.506 22699-22699/com.example.vietnguyen.myapplication E/AndroidRuntime: java.lang.RuntimeException: Unable to start activity ComponentInfo{com.example.vietnguyen.myapplication/com.example.vietnguyen.controllers.MainActivity}: java.lang.NullPointerException
11-30 03:05:32.506 22699-22699/com.example.vietnguyen.myapplication E/AndroidRuntime:

27. dialog builder notice error when back to app after tapping home btn