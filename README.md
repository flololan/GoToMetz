# README

## Run the app
- Open the project in Android Studio Code
- Build it
- Run it 

## Architecture

All the app is stored in `src/main/java/com/gooutinmetz` and `src/main/res/`.

The application is divided into 3 main activities: Map, Site and Category.
A common menu allows to navigate between the different activity.

Each activity refers to forms to handle the modification of data:
- `gooutinmetz/site/update/SiteForm.java` to handle the a

## Possible improvements

- (Prio 1) Avoid the crash if localisation is not set
- (Prio 1) Disable the "Add" button in SiteForm.java if data are not correct + check the input before validation 
- (Prio 2) Avoid the usage of BottomNavigationView.OnNavigationItemSelectedListener (deprecated) → check what is the best current practise
- (Prio 2) Avoid the usage of startActivityForResult in StateAddSite.java (deprecated) → check what is the best current practise
- (Prio 3) Avoid the switch in the Menu.java. Use a state pattern instead.


