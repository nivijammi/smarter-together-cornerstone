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
    analyticsPage: path.resolve(__dirname, 'src', 'pages', 'AnalyticsPage.js'),
    createAccountPage: path.resolve(__dirname, 'src', 'pages', 'CreateAccountPage.js'),
    createSessionPage: path.resolve(__dirname, 'src', 'pages', 'CreateSessionPage.js'),
    dashboardPage: path.resolve(__dirname, 'src', 'pages', 'DashboardPage.js'),
    indexPage: path.resolve(__dirname, 'src', 'pages', 'IndexPage.js'),
    myAccountPage: path.resolve(__dirname, 'src', 'pages', 'MyAccountPage.js'),
    myGroupPage: path.resolve(__dirname, 'src', 'pages', 'MyGroupPage.js'),
    notesPage: path.resolve(__dirname, 'src', 'pages', 'NotesPage.js'),
    searchSessionsPage: path.resolve(__dirname, 'src', 'pages', 'SearchSessionsPage.js'),
    signinPage: path.resolve(__dirname, 'src', 'pages', 'SigninPage.js'),
    studyGroupPage: path.resolve(__dirname, 'src', 'pages', 'StudyGroupPage.js')
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
          '/example',
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
                    })
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
