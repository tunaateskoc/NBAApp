# NbaApp
It is a NBAApp developed by Kotlin. It uses MVVM design pattern, Coroutines, Retrofit and JetPack libraries like Room, Lifecycle, ViewBinding, DataBinding, Hilt and Navigation.

# API
In this application, in order to access the data NBA API was used. It can be accessed with the following link https://rapidapi.com/theapiguy/api/free-nba/. 

# Summary
This project aims to get data of NBA Players and NBA Teams using API and save it into the database to show them as a list. Also, User can filter items using search bar or using filter icon, they are both located at the top of the page. User can see the detail of player or team by clicking that item. There is also state mechanism in order to control the states of the page like paging and error states.

# Description

The main page is divided into 2 different pages by using viewpager. In order to control filter mechanism, toolbar was used for both 2 pages. NBA Players and NBA Teams are listed in that pages. In order to get NBA Team icons, resource files were used.

<br>

<img src="https://user-images.githubusercontent.com/43845993/142883628-bd133c50-3688-4387-a43b-4848cf8eb8fa.png" width="200"> <img src="https://user-images.githubusercontent.com/43845993/142884797-aef40f60-6065-4346-8c6b-58174215e83d.png" width="200">


<br>

When the user clicks players or teams the detail page of that specific item is going to be displayed on the screen. 

<br>

<img src="https://user-images.githubusercontent.com/43845993/142885585-5453710d-9bd9-419f-b6f5-6f3faa0bbbda.png" width="200"> <img src="https://user-images.githubusercontent.com/43845993/142885603-3f4aff8b-ff2f-4ed4-95f7-8f68f8ab11f9.png" width="200">

<br>

User can filter that items using search bar on the top. It is going to filter players or teams using user input text, app checks whether the team name or player name contains that characters. Also, there is a another search mechanism. It opens by using filter icon on the top, for the players it filters with team and for the team it filters with a conference.

<br>

<img src="https://user-images.githubusercontent.com/43845993/142886473-2d36b49a-eb37-48a1-97a7-92a7a7724985.png" width="200"> <img src="https://user-images.githubusercontent.com/43845993/142886489-f689854b-62f7-4a6a-85e5-a8e0d02c1c3d.png" width="200">

<br>

There is also state management mechanism for the application. There are three type of states in that application. If the data comes right we will be at success state, when the data is requested from the API the application will go to loading state. When there is no internet and no data on the application it will go to error state and if there is no data coming from API it will pass to no data state. In our case the data is always coming from API so, we can not see the no data state.

<br>

<img src="https://user-images.githubusercontent.com/43845993/142887912-834fbf70-9de7-4c98-a1b8-f7287ab0f080.png" width="200"> <img src="https://user-images.githubusercontent.com/43845993/142887889-b1957adb-6f56-4249-bbc1-d8f48359e89e.png" width="200" height="350">

<br>

SwipeRefreshLayout was used in order to use pull to refresh mechanism to refresh the data. There is a pagination mechanism for Players because there are a lot of players in NBA History. The API provides NBA Player data with pagination mechanism. If the user scrolls to the bottom of the players page, pagination mechanism will work automatically and will get to next page data of the players.

<br>
