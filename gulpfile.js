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
        .pipe(rename({suffix: '.min'}))
        .pipe(cleanCSS({compatibility: 'ie8'}))
        .on('error', log)
        .pipe(cssFilter.restore)
        .pipe(dest(pathPrefix));
    cb();
}

function jsMinify(cb) {
    const jsFilter = filter(['**', '!**/*.min.js'], {restore: true});
    src(pathPrefix + '**/*.js')
        .pipe(jsFilter)
        .pipe(rename({suffix: '.min'}))
        .pipe(uglify())
        .on('error', log)
        .pipe(jsFilter.restore)
        .pipe(dest(pathPrefix));
    cb();
}

exports.default = parallel(
    cssMinify,
    jsMinify,
);
