import 'package:flutter/material.dart';

import '../pages/home_page.dart';
import '../pages/medications_page.dart';
import '../pages/notifications_page.dart';

class MainScreen extends StatefulWidget {
  const MainScreen({super.key});

  @override
  MainScreenState createState() => MainScreenState();
}

class MainScreenState extends State<MainScreen> {
  int _currentIndex = 1;

  final List<Widget> _screens = [
    const MedicationsPage(),
    const HomePage(),
    const NotificationsPage()
  ];

  @override
  Widget build(BuildContext context) {
    final bool isWearOS = MediaQuery.of(context).size.shortestSide < 300;

    return Scaffold(
      body: _screens[_currentIndex],
      bottomNavigationBar: isWearOS
          ? // For WearOS
      SizedBox(
        height: 32,
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: [
            IconButton(
              onPressed: () => _onTabTapped(0),
              icon: Icon(
                Icons.medication,
                color: _currentIndex == 0 ? Colors.indigo : Colors.grey,
                size: 20,
              ),
            ),
            IconButton(
              onPressed: () => _onTabTapped(1),
              icon: Icon(
                Icons.home,
                color: _currentIndex == 1 ? Colors.indigo : Colors.grey,
                size: 20,
              ),
            ),
            IconButton(
              onPressed: () => _onTabTapped(2),
              icon: Icon(
                Icons.notifications,
                color: _currentIndex == 2 ? Colors.indigo : Colors.grey,
                size: 20,
              ),
            ),
          ],
        ),
      )
          : // For mobile
      BottomNavigationBar(
        currentIndex: _currentIndex,
        onTap: (index) {
          setState(() {
            _currentIndex = index;
          });
        },
        items: const [
          BottomNavigationBarItem(
            icon: Icon(Icons.medication),
            label: 'Medications',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.home),
            label: 'Home',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.notifications),
            label: 'Notifications',
          ),
        ],
      ),
    );
  }

  void _onTabTapped(int index) {
    setState(() {
      _currentIndex = index;
    });
  }
}
