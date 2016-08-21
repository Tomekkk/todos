# TODO's Test Application
The repository contains example Android application which provides simple TODO's editor functionality. Project is written in [Kotlin](https://kotlinlang.org/) programming language. Architecture is based on [MVP](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter) architectural pattern and contains reactive programming approaches. Initial data is fetched on each application start from [http://jsonplaceholder.typicode.com/todos](http://jsonplaceholder.typicode.com/todos) 

##Main features
- Application fetch todos and allow load more by reaching last item on list.
- Each todo can be previewed.
- Editing todos (text and checkbox).
- Filter for not saved and changed todos.
- Not saved o todos 
- Saving todos to the API.

##Building
`./gradlew assembleDebug`

##Used libraries
- [Butter Knife](http://jakewharton.github.io/butterknife/) - Bind Android views and callbacks to fields and methods, library by Jake Wharton
- [Dagger 2](https://github.com/google/dagger) - A fast dependency injector for Android and Java, by Square and Google
- [EventBus](https://github.com/greenrobot/EventBus) - Android optimized event bus that simplifies communication between Activities, Fragments, Threads, Services, etc. Less code, better quality.
- [Gson](https://github.com/google/gson) - A Java serialization/deserialization library that can convert Java Objects into JSON and back by Google
- [Kotlin](http://kotlinlang.org/) - The Kotlin Programming Language by JetBrains by Markus Junginger
- [OkHttp](https://github.com/square/okhttp) - An HTTP+HTTP/2 client for Android and Java applications by Square, Inc.
- [RecyclerView-FlexibleDivider](https://github.com/yqritc/RecyclerView-FlexibleDivider) - Android library providing simple way to control divider items of RecyclerView by yqritc
- [Retrofit2](http://square.github.io/retrofit/)- Type-safe HTTP client for Android and Java by Square, Inc.
- [RxAndroid](https://github.com/ReactiveX/RxAndroid) specific binding for [RxJava](https://github.com/ReactiveX/RxJava) - Reactive Extensions for the JVM by Netflix, Inc. with Android bindings by The RxAndroid authors
- [Spock](https://github.com/spockframework/spock) - The Enterprise-ready testing and specification framework.

 