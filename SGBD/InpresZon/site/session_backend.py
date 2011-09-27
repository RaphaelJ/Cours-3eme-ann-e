# -*- coding: UTF-8 -*-

import datetime

from django.contrib.sessions.backends.base import CreateError
from django.contrib.sessions.backends.db import SessionStore as DBStore
from django.core.exceptions import SuspiciousOperation
from django.db import IntegrityError
from django.utils.encoding import force_unicode

from models import Session

class SessionStore(DBStore):
    """
    Enregistre les sessions de Django en utilisant des procédures PL/SQL
    """
    
    def load(self):
        try:
            s = Session.objects.get(
                cle = self.session_key,
                expiration__gt=datetime.datetime.now()
            )
            d = self.decode(force_unicode(s.donnees))
            print ("Données: "+ str(d))
            return d
        except (Session.DoesNotExist, SuspiciousOperation):
            self.create()
            return {}

    def exists(self, session_key):
        try:
            Session.objects.get(cle=session_key)
        except Session.DoesNotExist:
            return False
        return True

    def save(self, must_create=False):
        """
        Saves the current session data to the database. If 'must_create' is
        True, a database error will be raised if the saving operation doesn't
        create a *new* entry (as opposed to possibly updating an existing
        entry).
        """
        
        a = self._get_session(no_load=must_create)
        
        obj = Session(
            cle = self.session_key,
            donnees = self.encode(self._get_session(no_load=must_create)),
            expiration = self.get_expiry_date()
        )
        
        try:
            obj.save(force_insert=must_create)
        except IntegrityError:
            if must_create:
                raise CreateError
            raise

    def delete(self, session_key=None):
        if session_key is None:
            if self._session_key is None:
                return
            session_key = self._session_key
        
        try:
            Session.objects.get(cle=session_key).delete()
        except Session.DoesNotExist:
            pass