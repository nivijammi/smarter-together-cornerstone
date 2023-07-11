const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyPlugin = require("copy-webpack-plugin");
const { CleanWebpackPlugin } = require('clean-webpack-plugin');

module.exports = {
  optimization: {
    usedExports: true
  },
  entry: {
    examplePage: path.resolve(__dirname, 'src', 'pages', 'examplePage.js'),
    analyticsPage: path.resolve(__dirname, 'src', 'pages', 'analyticsPage.js'),
    createAccountPage: path.resolve(__dirname, 'src', 'pages', 'createAccountPage.js'),
    createSessionPage: path.resolve(__dirname, 'src', 'pages', 'createSessionPage.js'),
    dashboardPage: path.resolve(__dirname, 'src', 'pages', 'dashboardPage.js'),
    indexPage: path.resolve(__dirname, 'src', 'pages', 'IndexPage.js'),
    myAccountPage: path.resolve(__dirname, 'src', 'pages', 'myAccountPage.js'),
    myGroupPage: path.resolve(__dirname, 'src', 'pages', 'myGroupPage.js'),
    notesPage: path.resolve(__dirname, 'src', 'pages', 'notesPage.js'),
    searchSessionsPage: path.resolve(__dirname, 'src', 'pages', 'searchSessionsPage.js'),
    signinPage: path.resolve(__dirname, 'src', 'pages', 'signinPage.js'),
    studyGroupPage: path.resolve(__dirname, 'src', 'pages', 'studyGroupPage.js')
  },
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: '[name].js',
  },
  devServer: {
    https: false,
    port: 8080,
    open: true,
    proxy: [
      {
        context: [
          '/',
        ],
        target: 'http://localhost:5001'
      }
    ]
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: './src/analytics.html',
      filename: 'analytics.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/create-account.html',
      filename: 'create-account.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/create-session.html',
      filename: 'create-session.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/dashboard.html',
      filename: 'dashboard.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/index.html',
      filename: 'index.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/my-account.html',
      filename: 'my-account.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/my-group.html',
      filename: 'my-group.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/notes.html',
      filename: 'notes.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/search-sessions.html',
      filename: 'search-sessions.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/signin.html',
      filename: 'signin.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/study-group.html',
      filename: 'study-group.html',
      inject: false
    }),
    new CopyPlugin({
      patterns: [
        {
          from: path.resolve('src/css'),
          to: path.resolve("dist/css")
        }
      ]
    }),
    new CleanWebpackPlugin()
  ]
}
