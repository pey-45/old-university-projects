class Constants:
    _instance = None

    def __new__(cls):
        if cls._instance is None:
            cls._instance = super(Constants, cls).__new__(cls)

            cls._instance.DD_MM_DATES = ["es_ES", "gl_ES"]
            cls._instance.MM_DD_DATES = ["__lang__"]
        return cls._instance