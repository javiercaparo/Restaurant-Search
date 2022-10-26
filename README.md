# Restaurant Search

An Android application that utilises the [JustEat API](https://uk.api.just-eat.io/) to allow users to search for restaurants that deliver to their postcode.
Users can also opt to use their devices GPS to automatically locate their postcode.  

Built using Android Studio Dolphin | 2021.3.1

## Setup

### Ensure that you build the app using Java 11

`Android Studio -> Settings/Preferences -> Build, Execution, Deployment -> Build Tools -> Gradle -> Set "Gradle JDK" to '11'`

## Module structure

* `:app` - Code that launches the app & integration tests
* `:buildSrc`- Gradle configuration used across all modules (e.g. dependencies, versions, plugins, etc.)
* `:data` - Repositories that fetch data (using data sources) & map the responses to "clean" models
* `:network` - Data sources that fetch data over the network
* `:features` - Parent of submodules that represent application features
    * `:features:search` - Parent module of the search feature
        * `:features:search:ui` - The UI layer of the search feature (Composable's, ViewModel's)
        * `:features:search:domain` - The domain layer of the search feature (UseCase's)
    * `:features:requestlocation` - Parent module of the request location feature
        * `:features:requestlocation:ui` - The UI layer of the request location feature (Activity's, Activity contracts)
        * `:features:requestlocation:domain` - The domain layer of the request location feature (UseCase's)
* `:core` - Parent of submodules that contain logic that is used throughout the application
    * `:core:ui` - UI constants
    * `:core:models` - Model classes
    * `:core:test` - Unit test utility classes, also exposes all required unit test dependencies
