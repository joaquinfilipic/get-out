
	/**
	* Task-runner principal. Se encarga de transformar el proyecto
	* 'client-side' y de integrarlo dentro de 'server-side', para que la
	* aplicación pueda empaquetarse y desplegarse en un application-container.
	*
	* La ejecución debe ser idempotente.
	*/

/**
* Paths
*/
const dev = path => "./app/" + path;
const production = path => "../server-side/webapp/src/main/webapp/" + path;
const ignoring = path => "!" + path;
const lib = path => config.libs + "/" + path;
const withoutMin = path => path.basename = path.basename.replace(".min", "");

/**
* Support
*/
function readJSON(path) {
	return JSON.parse(fs.readFileSync(path, {
		encoding : "UTF-8",
		flag : "r"
	}));
}

/**
* Modules
*/
const composer = require("gulp-uglify/composer");
const cssMinify = require("gulp-clean-css");
const del = require("del");
const filter = require("gulp-filter");
const fs = require("fs");
const gulp = require("gulp");
const hash = require("gulp-rev");
const hashCleaner = require("gulp-rev-delete-original");
const htmlMinify = require("gulp-htmlmin");
const lint = require("gulp-jshint");
const rename = require("gulp-rename");
const replace = require("gulp-replace");
const requirejs = require("requirejs");
const rev = require("gulp-rev-collector");
const revFormat = require("gulp-rev-format");
const rewrite = require("gulp-rev-rewrite");
const sass = require("gulp-sass");
const uglifyjs = require("uglify-es");

const jsMinify = composer(uglifyjs, console);

/**
* Configurations
*/
const bower = readJSON(".bowerrc");
const config = {
	bundles : require("./require-build.js"),
	html : {
		minification : {
			caseSensitive : true,
			collapseWhitespace : true,
			decodeEntities : true,
			html5 : true,
			removeComments : true
		}
	},
	libs : bower.directory,
	revving : {
		file : "rev-manifest.json",
		path : dev("../")
	}
};
const libs = readJSON("lib.json");
sass.compiler = require("node-sass");

/**
* Filters
*/
function ignoreIndexFilter() {
	return filter(["**/*", ignoring("**/index.html")], {
			passthrough : true,
			restore : true
		});
}

function ignoreMinifiedFilter() {
	return filter(file => !/\.min/.test(file.path), {
			passthrough : true,
			restore : true
		});
}

function ignoreResourcesFilter() {
	return filter(file => !/\.json/.test(file.path), {
			passthrough : true,
			restore : true
		});
}

function onlyFilter(type) {
	return filter(file => new RegExp("\\." + type).test(file.path), {
			passthrough : true,
			restore : true
		});
}

/**
* Tasks
*/
function cleanTask(done) {
	del.sync([
		config.revving.path + config.revving.file,
		production("**/*"),
		ignoring(production("")),
		ignoring(production("WEB-INF/**"))
	], {
		force : true
	});
	done();
}

function imageTask(done) {
	return gulp.src([
			dev("images/**/*.gif"),
			dev("images/**/*.ico"),
			dev("images/**/*.jpg"),
			dev("images/**/*.png")
		])
		.pipe(gulp.dest(production("images/")));
}

function jsonTask(done) {
	return gulp.src([
			dev("**/*.json")
		])
		.pipe(gulp.dest(production("")));
}

function fontTask(done) {
	return gulp.src(dev("fonts/**/*"))
		.pipe(gulp.dest(production("fonts/")));
}

function scssTask(done) {
	return gulp.src(dev("scss/**/*.scss"))
		.pipe(sass.sync({
			outputStyle : "compressed"
		}).on("error", sass.logError))
		.pipe(gulp.dest(production("css/")));
}

function cssTask(done) {
	return gulp.src(dev("**/*.css"))
		.pipe(gulp.dest(production("")));
}

function cssLibTask(done) {
	const ignoreMinified = ignoreMinifiedFilter();
	return gulp.src(libs.css.map(lib))
		.pipe(ignoreMinified)
		.pipe(cssMinify({rebase : false}))
		.pipe(ignoreMinified.restore)
		.pipe(rename(withoutMin))
		.pipe(gulp.dest(production("lib/css/")));
}

function libTask(done) {
	const ignoreMinified = ignoreMinifiedFilter();
	return gulp.src(libs.js.map(lib))
		.pipe(ignoreMinified)
		.pipe(jsMinify())
		.pipe(ignoreMinified.restore)
		.pipe(rename(withoutMin))
		.pipe(gulp.dest(production("lib/")));
}

function jsTask(done) {
	return gulp.src([
			dev("../require-runtime.js"),
			dev("**/*.js")
		])
		.pipe(lint(".jshintrc"))
		.pipe(lint.reporter("jshint-stylish"))
		.pipe(rename(path => {
			if (path.basename === "require-runtime") {
				path.dirname += "/amd";
			}
		}))
		.pipe(gulp.dest(production("")));
}

function bundleTask(done) {
	requirejs.optimize(config.bundles,
		ok => {
			const time = new Date().toLocaleTimeString();
			console.log("[" + time + "] RequireJS finish without errors.");
			done();
		},
		error => console.log(error)
	);
}

function htmlTask(done) {
	return gulp.src([
			dev("**/*.html"),
			ignoring(dev("errors/**"))
		])
		.pipe(gulp.dest(production("")));
}

function errorTask(done) {
	return gulp.src(dev("errors/**/*.html"))
		.pipe(gulp.dest(production("errors/")));
}

function minifyTask(done) {
	const onlyCSS = onlyFilter("css");
	const onlyHTML = onlyFilter("html");
	const onlyJS = onlyFilter("js");
	return gulp.src([
			production("**/*.{css,html,js}"),
			ignoring(production("lib/**")),
			ignoring(production("WEB-INF/**"))
		])
		.pipe(onlyCSS)
		.pipe(cssMinify({rebase : false}))
		.pipe(onlyCSS.restore)
		.pipe(onlyHTML)
		.pipe(htmlMinify(config.html.minification))
		.pipe(onlyHTML.restore)
		.pipe(onlyJS)
		.pipe(jsMinify())
		.pipe(onlyJS.restore)
		.pipe(gulp.dest(production("")));
}

function revTask(done) {
	const ignoreIndex = ignoreIndexFilter();
	const ignoreResources = ignoreResourcesFilter();
	return gulp.src([
			production("**/*"),
			ignoring(production("WEB-INF/**"))
		])
		.pipe(ignoreIndex)
		.pipe(ignoreResources)
		.pipe(hash())
		.pipe(revFormat({
			prefix : ".",
			sufix : ".",
			lastExt : true
		}))
		.pipe(hashCleaner())
		.pipe(ignoreResources.restore)
		.pipe(ignoreIndex.restore)
		.pipe(gulp.dest(production("")))
		.pipe(hash.manifest(config.revving.file))
		.pipe(replace(/.+\.js.+/g, function (match) {
			const joint = match[match.length - 1] === ","? "\n" : ",\n";
			return match + joint + match.replace(/\.js/g, "");
		}))
		.pipe(gulp.dest(config.revving.path));
}

function rewriteTask(done) {
	const revManifest = gulp.src(dev("../rev-manifest.json"));
	return gulp.src([
			production("**/*"),
			ignoring(production("WEB-INF/**"))
		])
		.pipe(rewrite({
			manifest : revManifest
		}))
		.pipe(gulp.dest(production("")));
}

/**
* Runner
*/
gulp.task("clean", cleanTask);
gulp.task("resource",
	gulp.series(
		cleanTask,								// Cleaning
		gulp.parallel(
			imageTask, jsonTask, fontTask,		// Static resources
			scssTask, cssTask, cssLibTask,		// Style-sheets
			gulp.series(
				libTask, jsTask, bundleTask		// JavaScript
			),
			htmlTask, errorTask					// HTML
		)
	)
);
gulp.task("raw",
	gulp.series(
		"resource", revTask, rewriteTask		// File-revving
	)
);
gulp.task("default",
	gulp.series(
		"resource",
		minifyTask, revTask, rewriteTask		// Minification + File-revving
	)
);
