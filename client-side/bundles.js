
	/**
	* RequireJS bundles configuration.
	*/

module.exports = {
	mainModules : /js\/app.module/,
	modules : [
		{
			name : "js/app.module",
			create : false,
			include : ["lib/require"],
			exclude : []
		}
		/* MORE BUNDLES HERE */
	]
};
