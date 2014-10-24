 angular.module('apx-tools.filters', [])

 /**
  * @ngdoc filter
  * @name an-tools.filter:range
  * @param {integer} total The length of the array to be generated.
  * @return {array} An array with a size defined by the parameter.
  * @description
  * Return an array that can be used on ng-repeat. <br>
  * This array will have a size definied by the parameter and will contain integers from 0 to total-1.
  * # Example
  * <pre>
  * <div ng-repeat="i in [] | range:5"></div>
  * </pre>
  */
  .filter('range', function rangeFilter() { "use strict";
      return function(input, total) {
          total = parseInt(total, 10);
          for (var i = 0; i < total; i++) {
              input.push(i);
          }
          return input;
      };
  })

 /**
  * @ngdoc filter
  * @name an-tools.filter:padNumber
  * @param {integer|number} number The number to be left padded.
  * @param {integer} len The minimal amount of digits in the output number
  * @return {string} The input number left padded with a minimal of input len digits.
  * @description
  * Left pad the input number with '0' caracters.
  * # Example
  * <pre>
  * $filter('padNumber')(42,5); //return '00042'
  * $filter('padNumber')(4242,3); //return '4242'
  * </pre>
  */
  .filter('padNumber', function padNumberFilter() { "use strict";
      return function (number, len) {
          len = parseInt(len, 10);
          number = parseInt(number, 10);
          if (isNaN(number) || isNaN(len)) {
              return number;
          }
          var N = Math.pow(10, len);
          return number < N ? ("" + (N + number)).slice(1) : "" + number;
      };
  })


 /**
  * @ngdoc filter
  * @name an-tools.filter:capitalize
  * @param {string} input The input to be capitalized.
  * @return {string} The input capitalized on the first letter and with lowercase elsewhere.
  * @description
  * Return the input string in lowercase with the first letter capitalized.
  * # Example
  * <pre>
  * $filter('capitalize')('caPiTaLiZe Me'); //return 'Capitalize me'
  * </pre>
  */
  .filter('capitalize', function capitalizeFilter() { "use strict";

      return function(input) {
          if (input!=null && typeof input==='string' && input.length>0) {
              input = input.toLowerCase();
              return input.substring(0,1).toUpperCase()+input.substring(1);
          }
          else {
              return '';
          }
      };
  })




 /**
  * @ngdoc filter
  * @name an-tools.filter:orderObjectBy
  * @param {Object} object The object to sort
  * @param {string} field The field used by sort method
  * @param {boolean} reverse If true, sorting will be descending.
  * @return {array} Ordered array.
  * @description
  * Order an object properties and return them as an array.
  */
  .filter('orderObjectBy', function orderObjectByFilter($parse) { "use strict";
      return function(object, field, reverse) {
          var filtered = [];
          angular.forEach(object, function(item) {
              filtered.push(item);
          });
          filtered.sort(function(a, b) {
              var valA = $parse(field)(a);
              var valB = $parse(field)(b);
              return (valA > valB ? 1 : -1);
          });
          if (reverse) {
              filtered.reverse();
          }
          return filtered;
      };
  })
  
 ;

