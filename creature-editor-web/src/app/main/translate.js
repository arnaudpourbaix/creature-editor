/**
 * @description Contains all translations. Because french is the only supported language, there are not put in separate json files.  
 */
angular.module('editor.main.translate', []).config(function($translateProvider) {
	'use strict';
	$translateProvider.preferredLanguage('fr');
	$translateProvider.translations('fr', {
		"TITLE": "Pilotage Auchan France",
		"LOGOUT": "Déconnexion",
		"ERROR_MODAL" : {
			"TITLE" : "ERREUR",
			"DEFAULT_MESSAGE" : "Le serveur d'application a rencontré une erreur.<br/>Votre requête est inaccessible pour l'instant.",
			"RETRY" : "REESSAYER",
			"RESTART": "RELANCER L'APPLICATION"
		},			
		"ERROR": {
            "SECTOR_CONSOLIDATION": "Les indicateurs consolidés du besoin {{besoin}} sont disponibles uniquement pour un mois échu",
			"UNKNOWN_ERROR_CODE": "Code d'erreur inconnu: {{code}}",
			"OFFLINE": "Le serveur est déconnecté (404)",
			"TIMEOUT": "Le serveur n'a pas répondu dans les temps (timeout)",
            "TIMEOUT_FRIENDLY": "Le temps de réponse est anormalement élevé...",
			"CONVERT": "Problème de format de données",
			"TECHNICAL": "Le serveur a rencontré une erreur technique",
			"NOMENCLATURE_TURNOVER_REQUEST": "L'exécution de la requête des indicateurs nomenclatures a provoqué une erreur technique",
			"SITE_TURNOVER_REQUEST": "L'exécution de la requête des indicateurs sites a provoqué une erreur technique",
			"TOP_PRODUCT_REQUEST": "L'exécution de la requête des top produits a provoqué une erreur technique",
			"PERIOD_REQUEST": "La requête des périodes a provoqué une erreur technique",
			"GEOGRAPHIC_AXIS_REQUEST": "La requête des axes géographiques a provoqué une erreur technique",
			"PRODUCT_NOMENCLATURE_REQUEST": "La requête des nomenclatures produit a provoqué une erreur technique",
			"SALE_CHANNEL_REQUEST": "La requête des canaux de vente a provoqué une erreur technique",
			"NO_MARGIN_NOMENCLATURE_ERROR": "La requête des nomenclatures non margeables a provoqué une erreur technique",
			"NO_COMPANY_ID": "Pas de société sélectionnée",
			"NO_PERIOD": "Pas de période sélectionnée",
			"INVALID_DATES": "Dates invalides",
			"NOT_LOGIN": "Vous n'êtes pas connecté à l'application",
			"INVALID_URL": "Url invalide",
			"CACHE_UNKNOWN_REQUESTED_DATA": "Les données demandées n'existent pas",
			"NO_QUERY_DATA": "Pas de données",
			"UNMATCH_USER": "Vous ne pouvez pas manipuler les données d'un autre utilisateur",
			"NO_SEARCH_DATA": "Aucun élément ne correspond à votre recherche",
            "NO_FAVORITE_SITE": "Vous n'avez pas choisi de Sites favoris.",
            "NO_FAVORITE_MARKET": "Vous n'avez pas choisi de Marchés favoris.",
		}
	});
});	
