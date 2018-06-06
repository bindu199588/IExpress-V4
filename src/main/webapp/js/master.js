var app= angular.module("iexpress",["ngRoute", "ngMessages","ngMaterial","ui.router","ngMaterialDatePicker"]);
app.run(function ($rootScope,   $state,   $stateParams) {
    $rootScope.$state = $state;
    $rootScope.$stateParams = $stateParams;
});

/*
 * START APP CONFIG
 */

app.config(function($mdIconProvider) {
  $mdIconProvider
     .defaultIconSet('images/icons/icons.svg');
 });

/*
 *END APP CONFIG
 */

/*
 * START FACTORIES 
 */

app.factory('pageLoader',function(){
	var isLoading = false;
	return{
		setLoading: function(loading){
			isLoading = loading;
		},
		getLoading:function(){
			return isLoading;
		}
	}
	
});

app.factory('UserAuthenticationService',function($http,$state,$q){
	var thisFactory = {};
	
	thisFactory.encryptCookie = function(cookieString){	
		var config ={
				secretString : cookieString
		}
		return $http.post("rest/encryptString",config)
		.then(response => {
			return response.data;
		})
		.catch(err => console.log("ERROR IN ENCRYPTION"+err))
	}
	thisFactory.decryptCookie = function(cookieString){
		var config ={
				secretString : cookieString
		}
		return $http.post("rest/decryptString",config)
		.then(response => {
			return response.data
			
		})
		.catch(err => console.log("ERROR IN DECRYPTION"+err))
	}
	thisFactory.getLoginCookie = function(isAdmin,cookieName) {
		return $q((resolve,reject) =>{
//			if(!isAdmin){
//				 var name = "I-Express" + "=";
//			}
//			else{
//				 var name = "I-ExpressAdmin" + "=";
//			}
//			console.log("in getloginCookie");
			var name = cookieName;
		    var ca = document.cookie.split(';');
	    	for(var i = 0; i < ca.length; i++) {
		        var c = ca[i].trim();
		        if (c.indexOf(name) == 0) {
		            return resolve(c.substring(name.length, c.length));
		        }
		    }
	    	return resolve("");
		})
		
	}	
	thisFactory.setLoginCookie = function(cookieData,isAdmin,cookieName){
		return $q((resolve,reject) =>{
			var d = new Date();
			var expTime = 60*60*1000*24  // 1 day
			d.setTime(d.getTime() + expTime);
			var expiresCookie = "expires="+ d.toUTCString() + ";";
			thisFactory.encryptCookie(cookieData)
			.then(result => {
//				if(isAdmin){
//					var eventCookie = "I-ExpressAdmin" + "=" + result + ";" ;
//				}
//				else{
//					var eventCookie = "I-Express" + "=" + result + ";" ;
//				}
				var eventCookie = cookieName + result + ";" ;
				document.cookie = eventCookie + expiresCookie + "path=/"
				return resolve(true);
			})
			
			
		});
		
	}
	thisFactory.removeLoginCookie = function(cookieName){
		var d	= new Date(0);
		d.setTime(d.getTime());
		var expiresCookie = "expires="+ d.toUTCString() + ";";
		var eventCookie = cookieName + ";" ;
		document.cookie = eventCookie + expiresCookie + "path=/";
		return;
	}	
	thisFactory.loginUser = function(access_code){
		var config ={
				secretString: access_code
		}
		return $http({
		    method: 'POST',
		    url: 'rest/loginUser',
		    data: config
		})
		.then(function(response){
			if(response.data && response.data!=""){
				//console.log(response.data)
				cookieData = {
					id:response.data.id,
					name:response.data.name.trim(),
					desc:response.data.description.trim()
				}
				thisFactory.removeLoginCookie("I-ExpressAdmin=");
				return thisFactory.setLoginCookie(JSON.stringify(cookieData),false,"I-Express=")
				.then(result => {
					return true
				},(err)=>{
					console.log(err)
				})
			}
			else{
				return false;
			}
		},function(error){
			console.log("ERROR IN LOGGING IN USER");
		})	
	}
	thisFactory.loginAdmin = function(username,password){
		var config = {
			username: username,
			password: password
		}
		return $http.post("rest/loginAdmin",config)
		.then(function(response){
			if(response.data){
				thisFactory.removeLoginCookie("I-Express=");
				return thisFactory.setLoginCookie(JSON.stringify(response.data),true,"I-ExpressAdmin=")
				.then(result=>{
					return true;
				},(err)=>{
					console.log(err)
				})
				
			}
			else{
				return false;
			}
		},function(error){
			console.log("ERROR IN LOGGING IN ADMIN")
		})
	}
	
	thisFactory.isLoggedInAdmin = function(){
		return $q((resolve,reject) => {				
			return thisFactory.getLoginCookie(true,"I-ExpressAdmin=")
			.then(response =>{
				var existingCookie = response;
				if(existingCookie!=null && existingCookie !=""){
					return thisFactory.decryptCookie(existingCookie)
					.then(result=>{
						return resolve(result);
					})
				}
				else{
					return resolve(false);
				}
			});			
		})
	}
	thisFactory.checkEventLogin = function(){
		return $q((resolve,reject) => {				
			return thisFactory.getLoginCookie(false,"I-Express=")
			.then(response =>{
				var existingCookie = response;
				if(existingCookie!=null && existingCookie !=""){
					return thisFactory.decryptCookie(existingCookie)
					.then(result=>{
						return resolve(result);
					})
				}
				else{								
					return resolve("");					
				}
			})
		})	
	}
	thisFactory.logout = function(isAdmin){
		if(isAdmin){
			thisFactory.removeLoginCookie("I-ExpressAdmin=");
		}
		else{
			thisFactory.removeLoginCookie("I-Express=");
//			thisFactory.removeLoginCookie("QuestionLikes=");
//			thisFactory.removeLoginCookie("CommentLikes=");
		}
		return true;
	}	
	return thisFactory
});

/*
 * END FACTORIES
 */

/* 
 * START CONTROLLERS
 *  
 */


app.controller("indexController",function($rootScope,$scope){});

//////////////////////////////////////////////////////////////////////////////////////
////////////          CONTROLLER FOR ADMIN DASHBOARD     /////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////

app.controller("adminDashboardCtrl",function($scope,$q,$http,$state,$mdDialog,pageLoader,$state,$stateParams,UserAuthenticationService){	
	/*
	 * GET THE LIST OF ALL EXISTING EVENTS
	 */
	UserAuthenticationService.isLoggedInAdmin()
	.then(response => {
		if(!response){
			$state.go("adminLogin",{},{location:false});
		}
		else{
	    	$scope.isAdmin = true
	    	$scope.activeTagForDownload = null	    	
	    	$scope.userData=response;
	    	
	    	$scope.logout = function(){
	    		if(UserAuthenticationService.logout($scope.isAdmin)){
	    			$state.go("adminLogin",{},{location:false});
	    		}
	    		else{
	    			console.log("UNABLE TO LOGOUT")
	    		}    			
	    	}
	    	
	    	$scope.loadEvents = function(){
	    		$http.post("rest/getEventsFromAdminId",$scope.userData)
	    		.then((response)=>{
	    			$scope.groupEventList = response.data;
	    			console.log($scope.groupEventList);
	    			$scope.setThisEventActive(0,0);
	    		},(error)=>{
	    			console.log("THERE HAS BEEN AN ERROR IN QUERYING THE DATABASE"+error);
	    		})

			}	
	    	$scope.loadEvents();
	    	/* 
			 * SET THE SELECTED EVENT AS ACTIVE
			 */
			$scope.setThisEventActive = function(groupIndex,eventIndex){
				$scope.activeEventIndex = eventIndex;
				$scope.activeEventGroupIndex = groupIndex;
				
				$scope.activeEvent = $scope.groupEventList[groupIndex]['eventlist'][eventIndex];
				
				$scope.getTagsForEventId($scope.activeEvent['id']);
				$scope.getTimeLineGraph($scope.activeEvent['id'],null);
				$scope.currentTagsList = [];
			}
	    	/*
			 * GET ALL TAGS RELATED TO THE EVENT
			 */
			$scope.getTagsForEventId = function(eventId){		
				var config ={
						id : eventId
				}
				$http.post("rest/getTagsFromEventId",config)
				.then(function(response){
					$scope.tagsList = response.data
				},function(error){
					console.log("ERROR IN GETTING EVENT RELATED TAGS" + error);
				});
			}
	    	
	    	
	    	
	    	/*
	    	 * 
	    	 * CODE TO LOAD THE GROUPS DATA FOR ADMIN
	    	 * 
	    	 */
			
			
			
			
			$scope.loadGroups = function(){
				if($scope.userData.is_super){
					$http.post("rest/getAllGroups",$scope.userData)
					.then(function(response){
						$scope.groupList = response.data;
						$scope.setThisGroupActive(0);
					},function(error){
						console.log("THERE HAS BEEN AN ERROR IN QUERYING THE DATABASE"+error);
					});
				}
			}
			$scope.loadGroups();

			$scope.setThisGroupActive = function(groupIndex){
				$scope.activeGroupIndex = groupIndex;
				$scope.activeGroup = $scope.groupList[groupIndex];
				$scope.getAdminForGroup($scope.activeGroup);				
				$scope.unsavedAdminList = [];				
			}
			$scope.getAdminForGroup = function(groupData){
				$http.post("rest/getAdminInGroup",groupData)
				.then(function(response){
					$scope.adminList = response.data
				},function(error){
					console.log("ERROR IN GETTING EVENT RELATED TAGS" + error);
				});
			}
			
			$scope.showAdminPassword = function(ev,adminIndex){
				console.log($scope.adminList[adminIndex])
				$scope.adminList[adminIndex]['showPassword'] = true;
			}
			$scope.hideAdminPassword = function(ev,adminIndex){
				console.log($scope.adminList[adminIndex])
				$scope.adminList[adminIndex]['showPassword'] = false;
			}
			
			/*
			 * END OF GROUP DATA CODE
			 * 
			 */
			
			$scope.showAccessCode = function(ev,groupIndex,index){
				$scope.setThisEventActive(groupIndex,index)
				$scope.activeEvent['showAccess'] = true
			}
			
			
			
			$scope.getTimeLineGraph =function(eventId,tagId){
				var config = {
						params:{
							eventId:eventId,
							tagId : tagId,
						}
				}
				$http.get("rest/getEventGraphData",config)
				.then(response =>{
					var graphData = $scope.graphData = response.data;				
					var seriesArray =[];
					var names = {0:'UPSET',1:'SAD',2:'NEUTRAL',3:'HAPPY',4:'GLAD'}
					for(k=0;k<5;k++){
						seriesArray[k] = {name:names[k],data:[]};
					}
					
					for(i=0;i < graphData["0"].length;i++){
						totalCount = graphData["0"][i]['count'] + graphData["1"][i]['count'] + graphData["2"][i]['count'] + graphData["3"][i]['count'] +graphData["4"][i]['count']
						
						for(j = 0;j < 5;j++){
							tempObject = {
									x:new Date(graphData[j][i]['created_on']),
									y:parseFloat((graphData[j][i]['count']/totalCount) * 100)
							}
							seriesArray[j]['data'].push(tempObject);
						}
					}
					constructGraph(seriesArray);				
				})
				.catch(err=> console.log("ERROR GETTING GRAPH"+err));
			}
			
			/*
			 * DELETE TAG 
			 */
			
			$scope.deleteUnsavedTag = function(index){
				$scope.currentTagsList.splice(index,1);
			}
			
			
			
			var openEventModal = function(ev,groupList,eventData,create){
				return $q((resolve,reject)=>{
						$mdDialog.show({
							controller: 'ModifyEventModalController',
						    templateUrl: 'views/modals/modifyEventModal.html',
						    parent: angular.element(document.body),
						    targetEvent: ev,
						    clickOutsideToClose:true,
						    locals:{
						    	eventData:eventData,
						    	groupList: groupList,
						    	create:create
						     },
						    fullscreen: false
						})
						.then(function(response){
							if(response)
								return resolve(response)
							else 
								return reject()
							
						},function(){
							console.log("CREATION CANCELLED")
							return resolve();
							
						})
				})
			}
			$scope.updateEvent = function(ev,groupIndex,index,editMode){
				var modes ={
						'CREATE_NEW':0,
						'UPDATE':1,
						'DISABLE':2
				}
				
				switch(modes[editMode]){
					case 0:
						var eventData ={}
						openEventModal(ev,$scope.groupEventList,eventData,true)
						.then(eventResult => {
							if(eventResult){
								$http.post("rest/updateEvent",eventResult)
								.then(response=>{
									$scope.loadEvents();
								});								
							}
							else{
					    		console.log("NOTHING RETURNED FROM MODAL NEW CREATION")								
							}
						},function(){
							console.log("MODAL RESPONSE REJECTED")
						})
						break;
					case 1:
						$scope.setThisEventActive(groupIndex,index)
						var eventData = JSON.parse(JSON.stringify($scope.groupEventList[groupIndex]['eventlist'][index]));
						eventData.userGroup = {
								id: $scope.groupEventList[groupIndex].id
						}
						openEventModal(ev,null,eventData,false)
						.then(eventResult => {
							if(eventResult){
								$http.post("rest/updateEvent",eventResult)
								.then(response=>{
									$scope.loadEvents();
								});								
							}
							else{
					    		console.log("NOTHING RETURNED FROM MODAL NEW CREATION")								
							}
						},function(){
							console.log("MODAL RESPONSE REJECTED")
						})
						break;
					case 2:
						var eventData = JSON.parse(JSON.stringify($scope.groupEventList[groupIndex]['eventlist'][index]));
						eventData.userGroup = {
								id: $scope.groupEventList[groupIndex].id
						}
						eventData.is_active = !eventData.is_active
						$http.post("rest/updateEvent",eventData)
						.then(response=>{
							$scope.groupEventList[groupIndex]['eventlist'][index] = JSON.parse(JSON.stringify(eventData));
						});
						break;
				}
			}	
			
			var openTagModal = function(ev,tagData){
				return $q((resolve,reject) =>{
					$mdDialog.show({
						controller: 'ModifyTagModalController',
					    templateUrl: 'views/modals/modifyTagModal.html',
					    parent: angular.element(document.body),
					    targetEvent: ev,
					    clickOutsideToClose:true,
					    locals:{
					    	tagData:tagData
					     },
					    fullscreen: false
					})
					.then(function(response){
						if(response)
							return resolve(response)
						else 
							return reject()
						
					},function(){
						console.log("CREATION CANCELLED")
						return resolve();
						
					})
				})		      
			}
			$scope.updateTag = function(ev,index,editMode){
				var modes ={
						'CREATE_NEW':0,
						'EDIT_UNSAVED':1,
						'EDIT_SAVED':2,
						'DISABLE_TAG':3
				}
				switch(modes[editMode]) {
				    case 0:
				    	var tagData ={
				    		event:{
				    			id:$scope.activeEvent['id'],
				    			userGroup : {
					    				id:$scope.groupEventList[$scope.activeEventGroupIndex].id
					    		}
				    		}
				    	}
				    	var response = 
				    	openTagModal(ev,tagData)
				    	.then(response => {
				    		if(response)
				    			$scope.currentTagsList.push(response)
				    		else
					    		console.log("NOTHING RETURNED FROM TAG MODAL NEW CREATION")
				    	},function(){
				    		console.log("MODAL RESPONSE REJECTED")
				    	})
				    	break;
				    case 1:
				    	if(null!=index){				    		
				    		openTagModal(ev,$scope.currentTagsList[index])
					    	.then(response => {
					    		if(response)
					    			$scope.currentTagsList[index] = response
					    		else
						    		console.log("NOTHING RETURNED FROM MODAL NEW CREATION")
					    	},function(){
					    		console.log("MODAL RESPONSE REJECTED")
					    	})						    	
				    	}
				    	else{
				    		console.log("ERROR!! INDEX IS GIVEN AS NULL!!")
				    	}
				    	break;
				    case 2:
				    	if(null!=index){
					    	openTagModal(ev,$scope.tagsList[index])
					    	.then(response => {
					    		if(response){
					    			$http.post("rest/updateTag",response)
						    		.then(result=>{
						    			if(result){
						    				$scope.getTagsForEventId($scope.tagsList[index].event.id)
						    			}
						    			else{
						    				console.log("UPDATE RETURNED FALSE!! FAILED UPDATE")
						    			}
						    		})
						    		.catch(error => {
						    			console.log("ERROR WHILE UPDATING TAG IN DATABASE" + error)
						    		})
					    		}
					    		else{
					    			console.log("NOTHING RETURNED FROM MODAL NEW CREATION")
					    		}
						    		
					    	},function(){
					    		console.log("MODAL RESPONSE REJECTED")
					    	})
				    	}
				    	else{
				    		console.log("ERROR!! INDEX IS GIVEN AS NULL!!")
				    	}
				    	break;
				    case 3:
				    	if(null!=index){
				    		var tag = JSON.parse(JSON.stringify($scope.tagsList[index]));							
				    		tag.is_active = !tag.is_active
				    		tag.event.userGroup = {
				    				id:$scope.groupEventList[$scope.activeEventGroupIndex].id
				    		}
							$http.post("rest/updateTag",tag)
							.then(response=>{
								$scope.tagsList[index] = JSON.parse(JSON.stringify(tag));
							});
				    	}
				    	else{
				    		console.log("ERROR!! INDEX IS GIVEN AS NULL!!")
				    	}
				    	break;
				}
			}			
			$scope.saveTagChanges = function(){
				if($scope.currentTagsList.length >0 ){
					console.log($scope.currentTagsList)
					$http.post('rest/postAllNewTags',$scope.currentTagsList)
					.then(response => {
						$scope.currentTagsList = [];
						$scope.getTagsForEventId($scope.activeEvent['id']);
					})
					.catch(err=>console.log(err))
				}
				
			}
			
			/*
			 * MODAL CODE FOR GROUPS
			 * 
			 */
			
			$scope.openCreateGroupModal = function(ev){
				$mdDialog.show({
					  controller: 'createGroupModalController',
				      templateUrl: 'views/modals/createGroupModal.html',
				      parent: angular.element(document.body),
				      targetEvent: ev,
				      clickOutsideToClose:true,
				      fullscreen: false
				})
				.then(function(response){
					$scope.loadGroups();
				},function(){
					console.log("GROUP CREATION CANCELLED");
				});
			}
			$scope.openAddAdminToGroupModal = function(ev){
				$mdDialog.show({
					  controller: 'addAdminToGroupModalController',
				      templateUrl: 'views/modals/addAdminToGroupModal.html',
				      parent: angular.element(document.body),
				      targetEvent: ev,
				      clickOutsideToClose:true,
				      locals:{groupData:$scope.activeGroup},
				      fullscreen: false
				})
				.then(function(response){
					$scope.getAdminForGroup($scope.activeGroup);					
				},function(){
					$scope.getAdminForGroup($scope.activeGroup);
				});
			}
			$scope.removeUserFromGroup = function(){
				
			}
			/*
			 * END MODAL CODE FOR GROUPS
			 */
	
			$scope.openCreateAgendaModal = function(ev){
				$mdDialog.show({
					  controller: 'adminEventAgendaModalCtrl',
				      templateUrl: 'views/modals/adminEventAgendaModal.html',
				      parent: angular.element(document.body),
				      targetEvent: ev,
				      clickOutsideToClose:true,
				      locals:{eventData:{eventId : $scope.activeEvent['id']}},
				      fullscreen: true
				})
				.then(function(response){
					//$scope.currentTagsList.push(response)
				},function(){
					console.log("CREATION CANCELLED");
				});
			}
			
			/**
			 * 
			 * FILE DOWNLOAD METHODS
			 */
			var downloadDataAsFile = function(data,fileName){
			   var a = document.createElement('a');
			   a.href = 'data:attachment/csv;charset=utf-8,' + encodeURI(data);
			   a.target = '_blank';
			   a.download = fileName;
			   document.body.appendChild(a);
			   a.click();
			}
			$scope.downloadComments = function(){
				if($scope.activeTagForDownload){
					var tagId = $scope.activeTagForDownload['id']
					var tagName = $scope.activeTagForDownload['name'].trim()
				}
				var config = {
						params:{
							eventId : $scope.activeEvent['id'],
							tagId :   tagId || null
						}
				}
				$http.get("rest/exportComments",config)
				.then(response => {
					var fileName = 'CommentsList-['+ $scope.activeEvent['name'].trim() + '][' + (tagName || 'allTags') +'].txt';
					downloadDataAsFile(response.data,fileName);
				});
			}
			$scope.downloadQuestions = function(){
				var config = {
						params:{
							eventId : $scope.activeEvent['id']
						}
				}
				$http.get("rest/exportQuestions",config)
				.then(response => {
					var fileName = 'QuestionsList-['+ $scope.activeEvent['name'].trim() + '].txt';
					downloadDataAsFile(response.data,fileName);
				});
			}
		}
	})
	.catch(err=>console.log(err))
	
	
});

//////////////////////////////////////////////////////////////////////////////////////
////////////////          CONTROLLER FOR ADMIN LOGIN     /////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////
app.controller("adminLoginCtrl",function($scope,$state,UserAuthenticationService){
	UserAuthenticationService.isLoggedInAdmin()
	.then(response => {
		if(!response){
			$scope.authenticateAndGo= function(){
				if($scope.username && $scope.password){
					UserAuthenticationService.loginAdmin($scope.username,$scope.password)
					.then(response => {
						if(response){
							$state.go("adminDashboard",{userData:response})
						}
						else{
							$scope.errorMessages = {'accessError' : true}
						}
					})
				}
				else{
					$scope.errorMessages = {'lengthError' : true}
				}
				
			}
			
		}
		else{
			$state.go("adminDashboard",{userData:response});
		}
		
	})
	.catch(err=>console.log(err))
	
});


//////////////////////////////////////////////////////////////////////////////////////
////////////////CONTROLLER FOR USER LOGIN        /////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////

app.controller("userLoginCtrl",function($scope,$http,$mdDialog,$state,pageLoader,UserAuthenticationService){

			pageLoader.setLoading(false);
			$scope.askAccess = function(ev) {				
				$mdDialog.show({
					controller: 'eventLoginModalController',
					templateUrl: 'views/modals/eventLoginModal.html',
					parent: angular.element(document.body),
					targetEvent: ev,
					clickOutsideToClose:true,
					fullscreen: false
				})
				.then(function(match) {
					if(match){												
						$state.go('userDashboard');						
					}
				}, function() {
					console.log('You cancelled the dialog.');
				});
			}

});
//////////////////////////////////////////////////////////////////////////////////////
////////////////        CONTROLLER FOR USER DASHBOARD    /////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////
app.controller("userDashboardCtrl",function($scope,$http,$state,$stateParams,$mdDialog,pageLoader,UserAuthenticationService){
	pageLoader.setLoading(true);
	$scope.isAdmin = false	
	$scope.eventData = $stateParams.eventData;
	var showPage = function(){
		$scope.emoList=[
			{name:'upset',icon:'upset'},
			{name:'sad',icon:'sad'},
			{name:'neutral',icon:'neutral'},
			{name:'happy',icon:'happy'},
			{name:'glad',icon:'glad'}
		]
		var config ={
				params:{
					eventId : $scope.eventData.id
				}
		}
		$http.get('rest/allTagPercents',config)
			.then(function(response){
				$scope.hashtagList =response.data;
				pageLoader.setLoading(false);
			},function(error){
				console.log("ERROR IN GETTING RESPONSE")
			});
		
		$scope.goToPostScreen = function(index){
			pageLoader.setLoading(true);
			var selTagData = $scope.hashtagList[index];
			$state.go('userPostScreen', {tagId : selTagData.id,tagName:selTagData.name ,tagDesc:selTagData.desc,eventData:$scope.eventData});
		}
		$scope.logout = function(){
			if(UserAuthenticationService.logout($scope.isAdmin)){
				$state.go("userLogin",{},{location:false})
			}
			else{
				console.log("UNABLE TO LOGOUT")
			}
		}
		
		$scope.showAgenda = function(ev){
			$mdDialog.show({
				controller: 'userEventAgendaModalCtrl',
				templateUrl: 'views/modals/userEventAgendaModal.html',
				parent: angular.element(document.body),
				targetEvent: ev,
				clickOutsideToClose:true,
				locals:{eventData:$scope.eventData},
				fullscreen: true
			})
			.then(function(answer) {				
			}, function() {
				console.log('You cancelled the dialog.');
			});
		}
		
		$scope.showQuestions = function(ev){
			$mdDialog.show({
				controller: 'userQuestionsModalCtrl',
				templateUrl: 'views/modals/userQuestionsModal.html',
				parent: angular.element(document.body),
				targetEvent: ev,
				clickOutsideToClose:true,
				locals:{eventData:$scope.eventData},
				fullscreen: true
			})
			.then(function(answer) {				
			}, function() {
				console.log('You cancelled the dialog.');
			});
		}
	}
	showPage();

});


//////////////////////////////////////////////////////////////////////////////////////
////////////////        CONTROLLER FOR USER POSTSCREEN     ///////////////////////////
//////////////////////////////////////////////////////////////////////////////////////
//
app.controller("userPostScreenCtrl",function($scope,$interval, $mdDialog, $q,$state,$stateParams,$http,$timeout,pageLoader,UserAuthenticationService) {
		/*
		 * INIT ALL STATEPARAMS
		 * 
		 */
		pageLoader.setLoading(true);
        $scope.tagId = $stateParams.tagId;
        $scope.intervalPromise;
        $scope.selTweet='';
        $scope.userRatings ={}
        
        
        if($stateParams.tagName !==null){
        	$scope.tagName = $stateParams.tagName.trim();
        }
        if($stateParams.tagDesc !== null){
        	$scope.tagDesc = $stateParams.tagDesc;
        }
    	/*
    	 * END INIT STATEPARAMS
    	 * 
    	 */
    	$scope.isAdmin = false
    	$scope.logout = function(){
    		if(UserAuthenticationService.logout($scope.isAdmin)){
    			$state.go("userLogin",{},{location:false})
    		}
    		else{
    			console.log("UNABLE TO LOGOUT")
    		}
    	}
    	var showPage = function(){    		
    		$scope.curListTweets=[];
	    	$scope.prevListTweets=[];
	    	$scope.curTimeMS = 0;           	    	
	    	$scope.emoList=[            	    		
	    		{name:'upset',icon:'upset'},
	    		{name:'sad',icon:'sad'},
	    		{name:'neutral',icon:'neutral'},
	    		{name:'happy',icon:'happy'},
	    		{name:'glad',icon:'glad'}
	    	]
	    	
	    	$scope.saveUserRatings = function(){		
	    		UserAuthenticationService.setLoginCookie(JSON.stringify($scope.userRatings),false,"UserRatings=")
	    		.then(result => {
	    			//return true
	    		},(err)=>{
	    			console.log(err)
	    		})
	    	}
	    	
	    	$scope.loadAlreadyRated = function(){	
				console.log("in load if already rated");
				UserAuthenticationService.getLoginCookie(false,"UserRatings=")
				.then(response =>{
					var existingCookie = response;
					if(existingCookie!=null && existingCookie !=""){
						UserAuthenticationService.decryptCookie(existingCookie)
						.then(result=>{
							$scope.userRatings = result;
						})
					}
				})
			}
	    	
	    	$scope.preload=function(){	
	    		$scope.loadPercentgraph();
	    		$scope.getTweets($scope.curTimeMS,$scope.tagId);
	    		$scope.loadAlreadyRated();
    			$scope.intervalPromise = $interval(function(){
    				$scope.getTweets();
    			}, 2000);
	    	}

	    	$scope.$on('$destroy',function(){
	    	    if($scope.intervalPromise)
	    	        $interval.cancel($scope.intervalPromise);   
	    	});
	    	
	    	
	    	pageLoader.setLoading(false);
	    	
	    	$scope.askRatingConfirmation = function(ev,user_rating){
	    		$mdDialog.show({
					controller: 'userRatingConfirmModalCtrl',
					templateUrl: 'views/modals/userRatingConfirmModal.html',
					parent: angular.element(document.body),
					targetEvent: ev,
					clickOutsideToClose:true,
					locals:{
						ratingData:{
							tag_id:$scope.tagId,
							sentiment:user_rating,
							category:'RATING'
						}
					},
					fullscreen: false
				})
				.then(function(answer) {
					if(null!=answer && !answer){
						//SET AN ERROR MESSAGE
						console.log('ERROR WHILE POSTING THE RATING');
					}
					else{
						//RATING POSTED SUCCESSFULLY
						$scope.userRatings[$scope.tagId] = true;
						$scope.saveUserRatings();						
						$scope.loadPercentgraph();
						
					}
				}, function() {
					console.log('You cancelled the dialog.');
				});
	    	}
	    	
	    	$scope.postComment = function(){
	    		$scope.copyTweet = $scope.selTweet;
	    		$scope.selTweet='';
	    		
	    		if($scope.copyTweet.length>0){
	    			var filterconfig={
	    					question:$scope.copyTweet
	    			}
	    			var postconfig ={	    					
	    						tag_id:$scope.tagId,
	    						message:$scope.copyTweet,
	    						category:'COMMENT'
	    			}
	    			
	    			$http.post("rest/filterSwearWords",filterconfig)
	    			.then(response => {
	    				if(response.data){	    					
	    					$scope.errorMessages = {
	    							'swearWordsError':true
	    					}
	    				}
	    				else{	    					
	    					$scope.errorMessages = {}				
	    					$http.post("rest/postComment",postconfig)
	    	    			.then(function(response){
	    	    			});
	    				}
	    			})
	    			.catch(err=>{
	    				console.log("ERROR OCCURED WHILE FILTERING THE OBJECT");
	    			})
	    		}
	    		else{
	    			$scope.errorMessages = {
	    					'emptyError':true
	    			}
	    			console.log("CANNOT POST EMPTY STRING");
	    		}
	    	}
	    	
	    	$scope.resetTweets=function(){
	    		$scope.copyTweet=$scope.selTweet;
	    		$scope.selTweet='';
	    	}    	
	    	
		    $scope.getTweets=function(timeLastUpdate,tagId){ 
	    		var config={
	    				params:{
	    					curTimeMS:timeLastUpdate || $scope.curTimeMS,
	    					hashTag:tagId || $scope.tagId
	    				}
	    		}    			
				$http.get("rest/getTweets",config)
				.then(function(response){
					if(response.data.length>0){
						$scope.curTimeMS =Number(response.data[response.data.length - 1]['created_on']);						
						$scope.prevListTweets=$scope.curListTweets;
						$scope.curListTweets=$scope.prevListTweets.concat(response.data);						
						$timeout(function () {    						
							var objDiv = document.getElementById("tweetContainer");						
							objDiv.scrollTop = objDiv.scrollHeight+1000;    						
					    }, 0,false);						
						$scope.loadPercentgraph();
					}
				},function(error){
					console.log("THERE HAS BEEN AN ERROR IN QUERYING THE DATABASE"+error);
				});
		    }
		    
		    $scope.loadPercentgraph =function(){ 
		    	var config={
						params:{
							selTag:$scope.tagId
						}
				}
		    	$http.get("rest/perTagPercents",config)
				.then(function(response){
					$scope.emoPercents = response.data[0];
					if($scope.tagName == null){
						$scope.tagName = $scope.emoPercents['name'];
					}
					if($scope.tagDesc == null){
						$scope.tagDesc = $scope.emoPercents['desc'];
					}
				},function(error){
					console.log("THERE HAS BEEN AN ERROR IN QUERYING THE DATABASE"+error);
				});
		    }		    
		    $scope.preload();
    	}
    	showPage();

});


app.controller('userQuestionScreenCtrl',function($scope,pageLoader,UserAuthenticationService,$state,$stateParams,$http){
	$scope.logout = function(){
		if(UserAuthenticationService.logout($scope.isAdmin)){
			$state.go("userLogin",{},{location:false})
		}
		else{
			console.log("UNABLE TO LOGOUT")
		}
	}
});

//////////////////////////////////////////////////////////////////////////////////////
////////////////        CONTROLLER FOR TOP HEADER Component     ///////////////////////////
//////////////////////////////////////////////////////////////////////////////////////

app.controller("iexpressHeaderCtrl",function($scope){
	
	
});

//////////////////////////////////////////////////////////////////////////////////////
////////////////        CONTROLLER FOR LOADING PROGRESS Component     ////////////////
//////////////////////////////////////////////////////////////////////////////////////

app.controller("iexpressLoaderCtrl",function($scope,pageLoader,$timeout){
	$scope.activeLoading = function(){
		return pageLoader.getLoading();
	}
	
});

app.controller("eventAgendaComponentCtrl",function($scope,pageLoader,$timeout,$filter,mdcDateTimeDialog){
	
	$scope.activeLoading = function(){
		return pageLoader.getLoading();
	}
	$scope.openEditAgenda = function(){
		$scope.ctrl.agenda['showInput'] = true;
	}
	$scope.saveEditAgenda = function(){
		console.log($scope.ctrl.agenda)
		console.log($scope.curAgenda)
		console.log($scope)
		if($scope.curAgenda.agenda.length>0){
			
			console.log($scope.ctrl)
			$scope.ctrl.onUpdate({'index':$scope.ctrl.index,'agenda':$scope.curAgenda});
			
			$scope.ctrl.agenda['showInput'] = false;
			$scope.curAgenda=JSON.parse(JSON.stringify($scope.ctrl.agenda));
			$scope.displayTime ={};
			console.log("something happened")
		}
		else{
			
			$scope.errorMessages = {
					required:true
			}
		}

		
		
	}
	$scope.cancelEditAgenda = function(){
		$scope.curAgenda=JSON.parse(JSON.stringify($scope.ctrl.agenda));
		$scope.displayTime ={};
		$scope.ctrl.agenda['showInput'] = false;
	}
	$scope.displayDateDialog = function (nameOfTime) {
		var timeNow = new Date();
		if(nameOfTime == 'start_time'){
			var config = {
	          time: true
			}
		}
		else if(nameOfTime == 'end_time'){			
			var config = {
	          time: true
			}
		}
        mdcDateTimeDialog.show(config)
       .then(function (date) {        	  
    	  $scope.curAgenda[nameOfTime] = date.getTime()
    	  $scope.displayTime[nameOfTime] = $filter('date')(date, 'dd MMM yyyy hh:mm a');
       }, function() {
        console.log('Selection canceled');
       });
   }
	this.$onInit = function () {
	    $scope.ctrl = this;
	    $scope.curAgenda=JSON.parse(JSON.stringify($scope.ctrl.agenda));	    
		$scope.displayTime ={};
	}
	
});

//////////////////////////////////////////////////////////////////////////////////////
////////////////        CONTROLLER FOR USER EVENT LOGIN Modal     ////////////////////
//////////////////////////////////////////////////////////////////////////////////////

app.controller("eventLoginModalController",function($scope,$mdDialog,UserAuthenticationService){	
	$scope.codeLength = 5;
	$scope.accessCode = "";
	
	$scope.hide = function() {
	      $mdDialog.hide();
	};
    $scope.cancel = function() {
      $mdDialog.cancel();
    };
    
    $scope.answer = function(answer) {
    	if(answer.length == $scope.codeLength){
    		$scope.errorMessages = {'lengthError' : false};
    		UserAuthenticationService.loginUser(answer)
    		.then(access_granted => {
    			if(access_granted){
    				$mdDialog.hide(true);
        		}
        		else{
        			$scope.errorMessages = {'accessCodeError' : true};
        		}
    		})    		
    	}
    	else{
    		$scope.errorMessages = {'lengthError' : true};
    	}
    };
});


app.controller("createGroupModalController",function($scope,$http,$mdDialog){
	$scope.description ="";
	$scope.createGroup = function(){
		var groupData ={			
			group_name:$scope.group_name,
			description: $scope.description
		}
		
		$http.post("rest/createNewGroup",groupData)	
		.then(function(response){
			$mdDialog.hide("DONE");
		},function(error){
			$mdDialog.hide("UNABLE TO POST");
		});
	}
	
	$scope.hide = function() {
	      $mdDialog.hide();
	};
	
	$scope.cancel = function(){
		$mdDialog.cancel();
	}
});


app.controller("addAdminToGroupModalController",function($scope,$http,$mdDialog,groupData){
	
	$scope.addAdminToGroup = function(adminData){
		if(!adminData){
			adminData = {
					username:$scope.username
			}
		}
		if(adminData.username!="" && adminData.username!=null){
			var data = groupData
			data.adminlist = [adminData]
			$http.post("rest/addAdminToGroup",data)	
			.then(function(response){				
				$scope.errorMessages={
						'addSuccessMessage':true
				}
				$scope.username="";
				$scope.getAllExistingAdmin();
			},function(error){
				$mdDialog.hide("UNABLE TO POST");
			});
		}
		else{
			$scope.errorMessages = {
					'emptyError' : true
			}
		}
		
	}
	
	$scope.getAllExistingAdmin = function(){
		$http.post("rest/getAdminNotInGroup",groupData)	
		.then(function(response){
			console.log(response.data);
			$scope.allAdmin = response.data		
			
		},function(error){
			console.log("UNABLE TO GET THE LIST OF ADMIN");
		});
		
		
	}
	$scope.getAllExistingAdmin();
	
	$scope.hide = function() {
	      $mdDialog.hide();
	}	
	$scope.cancel = function(){
		$mdDialog.cancel();
	}
});

app.controller('adminEventAgendaModalCtrl',function($scope,$mdDialog,$http,$filter,eventData,mdcDateTimeDialog){

	$scope.unsavedAgendaList = [];
	$scope.savedAgendaList = [];
	$scope.curAgenda = {};
	$scope.displayTime = {}
	$scope.hide = function() {
	      $mdDialog.hide();
	};
	
	$scope.cancel = function(){
		$mdDialog.cancel();
	}
	
	$scope.addNewAgendaItem = function(){
		if($scope.curAgenda.agenda.length>0){
			$scope.showAgendaInput = false;
			$scope.curAgenda.event_id = eventData.eventId;
			$scope.unsavedAgendaList.push($scope.curAgenda);
			$scope.curAgenda = {};
			$scope.displayTime ={};
		}
		else{
			$scope.errorMessages = {
					required:true
			}
		}
	}
	
	$scope.deleteAgendaSaved = function(index,agenda){
		$http.post("rest/deleteAgenda",agenda)
		.then(response=>{
			$scope.getEventAgenda();
			console.log("DELETED!!!")
		})
	}
	
	$scope.editAgendaSaved = function(index,agenda){
		agenda.event = {
				id:eventData.eventId
		}
		$http.post("rest/saveAgenda",agenda)
		.then(response=>{
			$scope.getEventAgenda();
		})
	}
	$scope.deleteAgendaTemp = function(index,agenda){		
		$scope.unsavedAgendaList.splice(index, 1);	    
	}
	
	$scope.editAgendaTemp = function(index,agenda){		
		$scope.unsavedAgendaList[index]=agenda;
	}
	$scope.displayDateDialog = function (nameOfTime) {
		var timeNow = new Date();
//		if(nameOfTime == 'start_time'){
//			var config = {					
//			  minDate: $scope.curAgenda.start_time || new Date(timeNow.getFullYear(),timeNow.getMonth()-1,timeNow.getDate()),
//	          maxDate: $scope.curAgenda.end_time,
//	          time: true
//			}
//		}
//		else if(nameOfTime == 'end_time'){			
//			var config = {			          
//	          minDate: $scope.curAgenda.start_time || new Date(timeNow.getFullYear(),timeNow.getMonth()-1,timeNow.getDate()),
//	          time: true
//			}
//		}
		if(nameOfTime == 'start_time'){
			var config = {
	          time: true
			}
		}
		else if(nameOfTime == 'end_time'){			
			var config = {	
				time: true
			}
		}
        mdcDateTimeDialog.show(config)
       .then(function (date) {        	  
    	  $scope.curAgenda[nameOfTime] = date.getTime()
    	  $scope.displayTime[nameOfTime] = $filter('date')(date, 'dd MMM yyyy hh:mm a');
       }, function() {
        console.log('Selection canceled');
       });
   }
	
	$scope.cancelAgendaInput = function(){
			$scope.curAgenda = {};
			$scope.displayTime ={};
			$scope.showAgendaInput = false
	}
	
	$scope.saveAgenda = function(){		
		$http.post('rest/postEventAgenda',$scope.unsavedAgendaList)
		.then(response =>{
			$scope.getEventAgenda();
			$scope.unsavedAgendaList = [];
		})
		.catch(err => console.log(err))
	}
	
	$scope.getEventAgenda = function(){
		var config = {
				id: eventData.eventId				
		}
		$http.post('rest/loadEventAgenda',config)
		.then(response =>{
			$scope.savedAgendaList = response.data;
		})
	}
	$scope.getEventAgenda();
});

app.controller('userEventAgendaModalCtrl',function($scope,$mdDialog,$http,eventData){
	$scope.hide = function() {
	      $mdDialog.hide();
	};	
	$scope.cancel = function(){
		$mdDialog.cancel();
	};	
	$scope.getEventAgenda = function(){
		var config = {
			id: eventData.id				
		}
		$http.post('rest/loadEventAgenda',config)
		.then(response =>{
			$scope.agendaList = response.data;
		})
	};
	$scope.getEventAgenda();
});

app.controller('userQuestionsModalCtrl',function($scope,$mdDialog,$interval,$http,$q,$timeout,eventData,UserAuthenticationService){
	$scope.eventData = eventData;
	$scope.hide = function() {
	      $mdDialog.hide();
	};	
	$scope.cancel = function(){
		$mdDialog.cancel();
	}
	$scope.lastUpdatedTimeMS = 0;
	$scope.curListQuests = [];
	$scope.disableLikeList = {};
	
	$scope.postQuestion = function(userQuestion,eventId){
		
		var config={
				event_id : eventId || eventData.id,
				question : userQuestion
		}
		return $http.post("rest/filterSwearWords",config)
		.then(response => {
			if(response.data){
				console.log("QUESTION CONTAINS SWEAR WORDS. CANNOT POST !!");
				$scope.errorMessages = {
						'swearWordsError':true
				}
				return $q((resolve,reject) =>{
					resolve(false);
				});
			}
			else{				
				$scope.errorMessages = {}				
				return $q((resolve,reject) =>{
					$http.post("rest/postQuestion",config)
					.then(response =>{
						resolve(response);
					})
					.catch(err=>{
						console.log(err);
						resolve(false);
					})		
				})
			}
		})
		.catch(err=>{
			return $q((resolve,reject) =>{
				resolve(false);
			});
		})
	}
	$scope.askQuestion = function(){		
		if($scope.curUserQuestion && $scope.curUserQuestion.length > 0){
			var copyUserQuestion =$scope.curUserQuestion;
			$scope.curUserQuestion ="";
			$scope.postQuestion(copyUserQuestion,eventData.id)
			.then(response => {
				if(response){
						//show a toast for 5 seconds
						console.log("POSTED SUCCESSFULLY");
				}
				else{
					console.log("ERROR OCCURED WHILE POSTING.. ")
					//show toast with error message for 5 seconds
				}
			})
		}
		else{
			//show toast with error message for 5 seconds
			console.log("QUESTION CANNOT BE EMPTY");
		}	
	}
	
	
	$scope.loadAlreadyLikedQuestions = function(){	
		console.log("in load liked questions");
		UserAuthenticationService.getLoginCookie(false,"QuestionLikes=")
		.then(response =>{
			var existingCookie = response;
			if(existingCookie!=null && existingCookie !=""){
				UserAuthenticationService.decryptCookie(existingCookie)
				.then(result=>{
					$scope.disableLikeList = result;
				})
			}
		})
	}
	$scope.saveLikedQuestions = function(){		
		UserAuthenticationService.setLoginCookie(JSON.stringify($scope.disableLikeList),false,"QuestionLikes=")
		.then(result => {
			//return true
		},(err)=>{
			console.log(err)
		})
	}
	
	
	$scope.likeQuestion = function(index,questionId){
		
		//first disable the like button/first disable the like button
		$scope.disableLikeList[questionId] = true;		
		var config={
				id : questionId
		}
		
		$http.post('rest/likeQuestion',config)
		.then(response => {
			
			$scope.curListQuests[index] = response.data;
			var popListIndex = $scope.searchArray('id',response.data.id,$scope.popularQuestions)
			if(popListIndex>=0){
				$scope.popularQuestions[popListIndex] = response.data;
			}
			$scope.saveLikedQuestions();
		})
		.catch(err => {
			//do something // show a toast maybe
		})
	}
	$scope.likePopularQuestion = function(index,question){
		$scope.disableLikeList[question.id] = true;
		var config={
				id : question.id
		}		
		$http.post('rest/likeQuestion',config)
		.then(response => {
			/*
			 * check if the popular questions have not been pdated in the mean time
			 */
			if(response.data.id === $scope.popularQuestions[index].id){
				$scope.popularQuestions[index] = response.data;
			}
			/*
			 * update the live stream of questions also
			 */
			var curListIndex = $scope.searchArray('id',response.data.id,$scope.curListQuests)
			if(curListIndex>=0){
				$scope.curListQuests[curListIndex] = response.data;
			}
			$scope.saveLikedQuestions()
		})
		.catch(err => {
			//do something // show a toast maybe
		})	
		
	}
	
	$scope.searchArray = function(key,value,array){
		for(i=0;i<array.length;i++){
			if(parseInt(array[i][key]) === parseInt(value)){
				return i;
			}
		}
		return -1;
	}
	
	
	$scope.loadPopularQuestions = function(){
//		console.log("LOADING POPULAR QUESTIONS")
		var config={
				event_id:eventData.id
		}
		$http.post('rest/loadPopularQuestions',config)
		.then(response=>{
			$scope.popularQuestions = response.data;
		})
		.catch(err=>{
			console.log("ERROR WHILE LOADING POULAR QUESTIONS");
		})
	}
	
	
	$scope.loadQuestions = function(lastUpdateTimeMS){
		var config = {
				created_on : lastUpdateTimeMS || $scope.lastUpdatedTimeMS,
				event_id : eventData.id
		}		
		$http.post('rest/loadQuestions',config)
		.then(response =>{
			if(response.data && response.data.length>0){
				$scope.lastUpdatedTimeMS =Number(response.data[response.data.length - 1]['created_on']);
				$scope.curListQuests=$scope.curListQuests.concat(response.data);						
				$timeout(function () {    						
					var objDiv = document.getElementById("questionContainer");						
					objDiv.scrollTop = objDiv.scrollHeight+1000;    						
			    }, 0,false);
			}
			
		},function(err){
			console.log(err);
		})
	}
	
	//REFRESH QUESTIONS EVERY 2 SECONDS
	$scope.refreshQuestions =function(){
		$scope.intervalPromise = $interval(function(){
			$scope.loadQuestions();
		},2000)
	}
	
	$scope.refreshPopularQuestions =function(){
		$scope.popularIntervalPromise = $interval(function(){
			$scope.loadPopularQuestions();
		},1000*60)
	}
	
	$scope.preload = function(){
		$scope.loadQuestions($scope.lastUpdatedTimeMS);
		$scope.refreshQuestions();
		$scope.loadPopularQuestions();
		$scope.refreshPopularQuestions();
		$scope.loadAlreadyLikedQuestions();
	}
	
	$scope.preload();
	
	$scope.$on('$destroy',function(){
		if($scope.intervalPromise)
	        $interval.cancel($scope.intervalPromise);
			$interval.cancel($scope.popularIntervalPromise );
	});
});


app.controller('userRatingConfirmModalCtrl',function($scope,$http,$mdDialog,ratingData){
	
	$scope.ratingData = ratingData;
	$scope.postRating = function(){
		var postconfig ={	    					
				tag_id:ratingData.tag_id,
				sentiment:ratingData.sentiment,
				category:ratingData.category
		}
		$http.post("rest/postRating",postconfig)
		.then(function(response){
			$mdDialog.hide(true);
		})
		.catch(error=>{
			$mdDialog.hide(false);
		});
		
	}
	$scope.hide = function() {
	      $mdDialog.hide();
	};	
	$scope.cancel = function(){
		$mdDialog.cancel();
	}
	
	
});


app.controller('ModifyTagModalController',function($scope,$mdDialog,$http,tagData){
	
	$scope.tagData = tagData;
	$scope.updateTag = function(){
		$mdDialog.hide($scope.tagData);
	}
	
	$scope.hide = function() {
	      $mdDialog.hide();
	};
	
	$scope.cancel = function(){
		$mdDialog.cancel();
	}
})
app.controller('ModifyEventModalController',function($scope,$mdDialog,$http,eventData,groupList,create){
	
	$scope.eventData = eventData;
	$scope.userGroups = groupList;
	$scope.create = create;
	
	$scope.description ="";
	
	$scope.saveEvent = function(){
		$mdDialog.hide($scope.eventData);
	}
	
	$scope.hide = function() {
	      $mdDialog.hide();
	};
	
	$scope.cancel = function(){
		$mdDialog.cancel();
	}
})
///*
// * 
// * END CONTROLLERS
// * 
// * 
// */
