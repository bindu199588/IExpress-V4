var app= angular.module("iexpress");

app.config(function($stateProvider, $urlRouterProvider){

	$urlRouterProvider.otherwise("/userLogin");

	$stateProvider
	.state("adminLogin",{
		url:	"/adminLogin",
		templateUrl : "views/adminLogin.html",
		controller:"adminLoginCtrl",
		isAdmin : true,
		redirectPage: 'adminDashboard',
		params:{
			userData:null
		}
	})	
	.state("userLogin",{
		url:	"/userLogin",
		templateUrl : "views/userLogin.html",
		controller:"userLoginCtrl",
		isAdmin : false,
		redirectPage: 'userDashboard',
		params:{
			event: null
		}
	})
	.state("userDashboard",{
		url:	"/userDashboard",
		templateUrl : "views/userDashboard.html",
		controller:"userDashboardCtrl",
		params:{
			eventData : null
		},
		isAdmin : false,
		authenticate: true

	})	
	.state("userPostScreen",{
		url:	"/userPostScreen/:tagId",
		templateUrl : "views/userPostScreen.html",
		params: {
		        tagName: null,
		        tagDesc: null,
		        eventData : null
		},
		controller:"userPostScreenCtrl",
		isAdmin : false,
		authenticate: true

	})
	
	/*
	 * 
	 * ROUTES FOR ADMIN TABS
	 * 
	 */
	.state("adminDashboard",{
		url:	"/adminDashboard",
		templateUrl:	"views/adminDashboard.html",
		controller:"adminDashboardCtrl",
		isAdmin : true,
		authenticate: true
	})	
	
	.state('adminDashboard.groups', {
	    url: '/groups',
	    data: {
	      'selectedTab': 0
	    },
	    views: {
	      'groups': {
	    	controllerAs: 'vm',
	        controller: "adminGroupsTabCtrl",
	        templateUrl: 'views/admin/groups.html'
	        	
	      }
	    }
	  })
	.state('adminDashboard.events', {
	    url: '/events',
	    data: {
	      'selectedTab': 1
	    },
	    views: {
	      'events': {
	    	controllerAs: 'vm',
	        controller: "adminEventsTabCtrl",
	        templateUrl: 'views/admin/events.html'
	      }
	    }
	  })
	  .state('adminDashboard.reviews', {
	    url: '/reviews',
	    
	    data: {
	      'selectedTab': 2
	    },
	    views: {
	      'reviews': {
	    	controllerAs: 'vm',
	        controller: "adminReviewsTabCtrl",
	        templateUrl: 'views/admin/reviews.html'
	      }
	    }
	  })
	//$urlRouterProvider.deferIntercept(true);
});
