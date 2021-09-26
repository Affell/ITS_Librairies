from Interface import interface
from zope.interface import implements, Interface


class Event(Interface):
    def __init__(self):
        self.name = None

    @interface.default
    def getName(self):
        if self.name is None:
            return Interface.__name__
        else:
            return self.name
