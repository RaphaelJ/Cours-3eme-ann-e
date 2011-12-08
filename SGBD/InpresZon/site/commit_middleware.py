# -*- coding: UTF-8 -*-

from django.db import connection

class CommitMiddleware:
    """
    Effectue un commit général dans la base de données si des modifications
    ont été effectuées.
    """
    
    def process_response(self, request, response):
        if connection.connection != None:
            connection.connection.commit()

        return response