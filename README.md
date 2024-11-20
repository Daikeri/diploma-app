# Streaming online movie theater

# :hammer_and_wrench: Languages and tools :
<div>
  <img src='https://github.com/devicons/devicon/blob/master/icons/kotlin/kotlin-original.svg' width="100" height="100">
  <img src='https://github.com/devicons/devicon/blob/master/icons/androidstudio/androidstudio-original-wordmark.svg' width="100" height="100">
</div>

## The idea
Initially, this project acted as a thesis on the topic "Development of a service for personalized recommendations of media content". In addition to implementing a mobile application, my tasks included implementing a set of ML models, as well as writing a number of FastAPI/Ktor services to process user actions. At the moment, this project is more of a simulator for complex development of acquired skills in Android development.
At the moment, the main focus is on the transition to a multi-module architecture using such DI frameworks as Dagger2 and Hilt.

## Application features

### Authorization
The Firebase Authentication service was used to implement authorization. After satisfying the regular expressions of the form, an attempt is made to log in/register. The exceptions that are called are caught and result in a SnackBar.

| <img src="https://github.com/Daikeri/diploma-app/blob/master/readmecontent/select_v1.jpg" alt="Описание 1" width="288" height="640"> | <img src="https://github.com/Daikeri/diploma-app/blob/master/readmecontent/select_v2.jpg" alt="Описание 1" width="288" height="640"> |
|:---------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------:|
| Select v1                                                               | Select v2                                                               |

| <img src="https://github.com/Daikeri/diploma-app/blob/master/readmecontent/auth_v1.jpg" alt="Описание 1" width="288" height="640"> | <img src="https://github.com/Daikeri/diploma-app/blob/master/readmecontent/auth_v2.jpg" alt="Описание 1" width="288" height="640"> |
|:---------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------:|
| Auth v1                                                               | Auth v2                                                               |

### The main feed of recommendations
The classic pattern of any modern online cinema is the recommendation feed. Each feed contains the most relevant films in its category. Posters and other meta information are uploaded using the ODMb service. The API is accessed using the Retrofit library. An attempt was also made to implement skeletal loading. It's worth working on the dynamics of the animation.

https://github.com/user-attachments/assets/0a17f2e5-ef2a-4b5f-9af4-b6a284dd88ac

When you select a movie card, a detailed version opens. The detailed card contains all the meta information and ways to evaluate the film.
The original version only performed the display function with a terrible design. 

| <img src="https://github.com/Daikeri/diploma-app/blob/master/readmecontent/card_detail_v1.jpg" alt="Описание 1" width="288" height="640"> |
|:---------------------------------------------------------------------------------------------:|
| Card Detail v1                                                              |

#### Card Detail v2
https://github.com/user-attachments/assets/66fb24fb-9ce8-483f-bd09-fe15d819d0e3

### Search
The search for films by name was also implemented. Initially, the search is performed using the local Sqllite database. To do this, it was necessary to use the Room library to create a layer between Kotlin and SQL queries.

https://github.com/user-attachments/assets/272e8902-cc20-466e-ad30-dae505ab5b51
