const {src, dest, parallel} = require('gulp');
const filter = require('gulp-filter');
const uglify = require('gulp-uglify');
const rename = require('gulp-rename');
const cleanCSS = require('gulp-clean-css');
const log = require('fancy-log');

const pathPrefix = './src/main/resources/static/plugins/';

function cssMinify(cb) {
    const cssFilter = filter(['**', '!**/*.min.css'], {restore: true});
    src(pathPrefix + '**/*.css')
        .pipe(cssFilter)
		.pipe(cleanCSS({compatibility: 'ie8',debug: true}, (details) => {
			console.log(`${details.name}: ${details.stats.originalSize}`);
			console.log(`${details.name}: ${details.stats.minifiedSize}`);
		}))
        .pipe(rename({suffix: '.min'}))
        .on('error', log)
        .pipe(dest(pathPrefix));
    cb();
}

function jsMinify(cb) {
    const jsFilter = filter(['**', '!**/*.min.js']);
    src(pathPrefix + '**/*.js')
        .pipe(jsFilter)
		.pipe(uglify())
        .pipe(rename({suffix: '.min'}))
        .on('error', log)
        .pipe(dest(pathPrefix));
    cb();
}

exports.default = parallel(
    cssMinify,
    jsMinify,
);
