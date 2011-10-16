# -*- coding: UTF-8 -*-

from django.db import connection

class CommitMiddleware:
    """
    Effectue un commit général dans la base de données si des modifications
    ont été effectuées.
    """
    
    def process_response(self, request, response):
        print ("try Commit")
        if connection.connection != None:
            print ("Commit")
            connection.connection.commit()

        return response