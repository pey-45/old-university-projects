class ConnectionException implements Exception {
  final String message;

  ConnectionException(this.message);

  @override
  String toString() => "ConnectionException: $message";
}