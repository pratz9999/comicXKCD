# Comics Explorer

Sample project to demonstrate the consumption of API from Comic XKCD.

### Technologies
* `Architecture`: MVVM with Clean Architecture
* `Database`: Room
* `DI`: Koin
* `Thread system`: Kotlin coroutines
* `Network`: Retrofit
* `Other`: Data/ViewBinding, Binding Adapters, LiveData, Kotlin Flow, Jetpack Nav, Extension functions.

---

### Tasks

- [x] browse through the comics,
- [x] see the comic details, including its description,
- [x] search for comics by the comic number as well as the the comic title,
- [x] get the comic explanation
- [x] favorite the comics, which would be available offline too,
- [x] send comics to others,
- [ ] get notifications when a new comic is published ''(Looks like a server is needed so skipped for the MVP),''
- [ ] support multiple form factors.(Time constraints)


### Important Notes

* Due to time constraints I could not complete all the tasks down to top, thus I tried to showcase them.
* Tried to keep the whole application testable but decided not to move on with TDD due to the project ambiguity.
* I covered uncommon codes with comments, However skipped most of the code to save the time.
* Used NavGraph for navigation and it should be implemented with FragmentFactory when adding more interactions with the fragments.
* Used Individual ViewModel as per modularity

