# Good solution at all and also an efficient approach going with one model. 

# Annotations: 
 - Be careful when the size of the MainCroller increases. Could be an issue when more engineers should work on the system. 

# Improvements: 
 - You could bind the text on the buttons to a text property of the model. So if the state of the model changes (e.g. you are entering the edit mode) the text is automatically updated. 
 - You could also bind the enabling and disabling of controls to properties of the model. 
 - Don't rely on the texts of buttons by evaluating the state of you app. Imagine if someone changes the design by renaming a button it would break. Also have in mind that the app should (maybe in future) support multiple languages, which makes it difficult to test against the texts of the controls. (compare line 268 in MainController (onCurrentAction method). 
 - Ln 470: you could make it easier for the model deleting a tour by offering a proper method (e.g.: deleteSelectedTour). The model can figure our which tour is currently selected on it's own. 
 - Think about using a controller factory for the xml_views used for the separate windows. This is the way the report controller could get access to the proper report model. Whenaver a new window is opened the factory will create a new controller and pass the proper model instance into it's constructor. 

20 points