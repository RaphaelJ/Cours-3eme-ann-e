# -*- coding: UTF-8 -*-

import datetime

from django.contrib.sessions.backends.base import CreateError
from django.contrib.sessions.backends.db import SessionStore as DBStore
from django.core.exceptions import SuspiciousOperation
from django.db import IntegrityError
from django.utils.encoding import force_unicode

from models import Session
from packages import GestionSessions

class SessionStore(DBStore):
    """
    Enregistre les sessions de Django en utilisant des procédures PL/SQL
    """
    
    def load(self):
        try:
            s = GestionSessions.Chercher(self.session_key)
            d = self.decode(force_unicode(s.donnees))
            return d
        except:
            self.create()
            return {}

    def exists(self, session_key):
        try:
            GestionSessions.Chercher(session_key)
            return True
        except Exception as e:
            return False

    def save(self, must_create=False):
        """
        Saves the current session data to the database. If 'must_create' is
        True, a database error will be raised if the saving operation doesn't
        create a *new* entry (as opposed to possibly updating an existing
        entry).
        """

        s = Session(
            cle = self.session_key,
            donnees = self.encode(self._get_session(no_load=must_create)),
            expiration = self.get_expiry_date()
        )
        
        try:
            if must_create or not self.exists(s.cle):
                GestionSessions.Ajouter(s.cle, s.donnees, s.expiration)
            else:
                GestionSessions.Modifier(s.cle, s.donnees, s.expiration)
        except:
            if must_create:
                raise CreateError
            raise

    def delete(self, session_key=None):
        if session_key is None:
            if self._session_key is None:
                return
            session_key = self._session_key
        
        GestionSessions.Supprimer(session_key)