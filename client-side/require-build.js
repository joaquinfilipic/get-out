
	/**
	* Configuración build-time de RequireJS, utilizada para construir los
	* bundles de JavaScript, mediante Gulp.
	*
	* @see https://github.com/requirejs/r.js/blob/master/build/example.build.js
	*/

const bundles = require("./bundles.js");

// Configuración final para RequireJS:
module.exports = {

	/**
	* General Options
	*/
	allowSourceOverwrites : false,
	baseUrl : "app/",
	cjsTranslate : false,
	dir : "../server-side/webapp/src/main/webapp/",
	fileExclusionRegExp : /\./,
	findNestedDependencies : true,
	generateSourceMaps : false,
	inlineText : true,
	keepAmdefine : false,
	keepBuildDir : true,
	locale : "es-ar",
	logLevel : 0,
	modules : bundles.modules,
	namespace : "",
	nodeIdCompat : true,
	normalizeDirDefines : "skip",
	optimize : "none",
	optimizeAllPluginResources : false,
	optimizeCss : "none",
	preserveLicenseComments : false,
	removeCombined : false,
	skipDirOptimize : true,
	skipModuleInsertion : false,
	skipPragmas : true,
	skipSemiColonInsertion : true,
	useSourceUrl : false,
	useStrict : false,
	waitSeconds : 0,
	wrapShim : false,
	writeBuildTxt : false,

	/**
	* Input/Output Rewriting
	*/
	onBuildRead : (module, path, content) => {
		if (module.match(bundles.mainModules) !== null) {
			console.log("\tAdding bootstrap to main-module: " + module + ".");
			content += "\n\trequire([\"amd/require-runtime\"], function (r) {";
			content += "\n\t\trequire([\"" + module + "\"]);";
			content += "\n\t});\n";
		}
		return content;
	},
	onBuildWrite : (module, path, content) => {
		return content;
	},
	onModuleBundleComplete : (data) => {
	},
	paths : {
		"lib/angular" : "empty:",
		"lib/angular-animate" : "empty:",
		"lib/angular-base64-upload" : "empty:",
		"lib/angular-resource" : "empty:",
		"lib/angular-route" : "empty:",
		"lib/angular-toastr.tpls" : "empty:",
		"lib/bootstrap" : "empty:",
		"lib/bootstrap-datepicker" : "empty:",
		"lib/domReady" : "empty:",
		"lib/jquery" : "empty:",
		"lib/lodash" : "empty:",
		"lib/ng-map" : "empty:",
		"lib/ngStorage" : "empty:",
		"lib/popper" : "empty:",
		"lib/angular-translate" : "empty:",
		"lib/angular-translate-loader-partial" : "empty:"
	},
	wrap : {
		start : "(function () {",
		end : "}) ();"
	}
}
