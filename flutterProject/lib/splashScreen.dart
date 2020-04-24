import 'dart:async';
import 'package:flutter/material.dart';
import 'loginPage.dart';

void main() => runApp(MaterialApp(
      theme:
          ThemeData(primaryColor: Colors.red, accentColor: Colors.yellowAccent),
      debugShowCheckedModeBanner: false,
      home: SplashScreen(),
    ));

class SplashScreen extends StatefulWidget {
  @override
  _SplashScreenState createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> {
  @override
  void initState() {
    super.initState(); //metodo para llamar a login page, despues de 5 segundos iniciada el splash screen
    Timer(
        Duration(seconds: 5),
        () => Navigator.push(
              context,
              MaterialPageRoute(builder: (context) => LoginPage()),
            ));
  }

  @override
  Widget build(BuildContext context) {
    var children2 = <Widget>[
      Container(
        decoration: BoxDecoration(color: Colors.white),
      ),
      Column(
        mainAxisAlignment: MainAxisAlignment.start,
        children: <Widget>[
          Expanded(
            flex: 2,
            child: Container(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  Image.asset("img/login.png"),
                  Padding(
                    padding: EdgeInsets.only(top: 10.0),
                  ),
                ],
              ),
            ),
          ),
          Expanded(
            flex: 1,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                CircularProgressIndicator(),
                Padding(
                  padding: EdgeInsets.only(top: 20.0),
                ),
                Text(
                  "PREVCRIM",
                  style: TextStyle(
                      color: Colors.blue,
                      fontSize: 24.0,
                      fontWeight: FontWeight.bold),
                )
              ],
            ),
          )
        ],
      )
    ];
    return Scaffold(
      body: Stack(
        fit: StackFit.expand,
        children: children2,
      ),
    );
  }
}
