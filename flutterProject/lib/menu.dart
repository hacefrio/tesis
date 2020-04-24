// Flutter code sample for FloatingActionButton

// This example shows how to display a [FloatingActionButton] in a
// [Scaffold], with a pink [backgroundColor] and a thumbs up [Icon].
//
// ![](https://flutter.github.io/assets-for-api-docs/assets/material/floating_action_button.png)

import 'package:flutter/material.dart';

void main() => runApp(Menu());

/// This Widget is the main application widget.
class Menu extends StatelessWidget {
  static const String _title = 'PREVCRIM';

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: _title,
      home: MyStatelessWidget(),
    );
  }
}

/// This is the stateless widget that the main application instantiates.
class MyStatelessWidget extends StatelessWidget {
  MyStatelessWidget({Key key}) : super(key: key);

Widget renderUsuariosButton(){
  return Container(
    padding: const EdgeInsets.symmetric(horizontal: 32, vertical: 20),
    child: RaisedButton(child: Text('Usuarios'),
    onPressed:(){} ,
    color: Colors.blue, )
  );
}
Widget renderInstitucionButton(){
  return Container(
    padding: const EdgeInsets.symmetric(horizontal: 32,vertical: 20),
    child: RaisedButton(child: Text('Instituciones'),
    onPressed:(){} , 
    color: Colors.blue,)
  );
}

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('PREVCRIM'),
      ),
      body: Container(
        padding: const EdgeInsets.symmetric(),
        decoration: BoxDecoration(color: Colors.grey),
        child: ListView(
          children: [
            renderUsuariosButton(),
            renderInstitucionButton()
          ],
        ),
      ),
    );
  }
}
