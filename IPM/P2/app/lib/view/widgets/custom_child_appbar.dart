import 'package:flutter/material.dart';

class CustomChildAppBar extends StatelessWidget implements PreferredSizeWidget {
  final String title;

  const CustomChildAppBar({super.key, required this.title});

  @override
  Widget build(BuildContext context) {
    final bool isWearOS = MediaQuery.of(context).size.shortestSide < 300;

    return isWearOS
        ? Padding(
      padding: const EdgeInsets.symmetric(horizontal: 6.0, vertical: 3.0),
      child: Row(
        children: [
          GestureDetector(
            onTap: () {
              Navigator.of(context).pop();
            },
            child: const Icon(
              Icons.arrow_back,
              color: Colors.indigo,
              size: 20,
            ),
          ),
          const SizedBox(width: 6),
          Text(
            title,
            style: const TextStyle(
              color: Colors.indigo,
              fontSize: 14,
              fontWeight: FontWeight.bold,
            ),
          ),
          const Spacer(),
        ],
      ),
    )
        : AppBar(
      title: Text(
        title,
        style: const TextStyle(
          color: Colors.white,
          fontSize: 24,
        ),
      ),
      backgroundColor: Colors.indigo,
      leading: IconButton(
        onPressed: () {
          Navigator.of(context).pop();
        },
        icon: const Icon(
          Icons.arrow_back,
          color: Colors.white,
          size: 28,
        ),
      ),
    );
  }

  @override
  Size get preferredSize => const Size.fromHeight(kToolbarHeight);
}
