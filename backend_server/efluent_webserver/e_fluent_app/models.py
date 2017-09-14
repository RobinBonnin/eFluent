from django.db import models

# Create your models here.

from django.contrib.auth.models import User


class CustomUser(User):
    class Meta:
        proxy = True

    # def __str__(self):
    #     return self.name.upper()

    def get_role(self):
        ortho = getattr(self, 'orthophoniste', None)
        patient = getattr(self, 'patient', None)
        return ortho if ortho else patient


class Orthophoniste(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE, null=False)
    #meeting = models.ForeignKey('Meeting', null=True, blank=True)

    role_name = "Orthofoniste"

    def __str__(self):
        return self.user.username

    def get_meetings(self):
        return Meeting.objects.filter(orthophoniste=self)

    def get_exercises(self, pk=None):
        # return AssignedExercise.objects.filter(patient__orthophoniste=self, patient__id=pk)
        return AssignedExercise.objects.filter(patient__id=pk)

class Patient(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE, null=False)
    orthophoniste = models.ForeignKey(Orthophoniste, null=True, blank=True)
    #meeting = models.ForeignKey('Meeting', null=True, blank=True)

    role_name = "Patient"
	
    def __str__(self):
        return self.user.username

    def get_meetings(self):
        return Meeting.objects.filter(patient=self)

    def get_exercises(self):
        return AssignedExercise.objects.filter(patient=self)

#Signals to create a patient #TODO

class Exercise(models.Model):
    # This are created by the User
    name = models.CharField(max_length=200, null=False, blank=False)
    #Audio = URL
    #Category

    def __str__(self):
        return self.name

class AssignedExercise(models.Model):
    # This are created by the Orthoponiste and done by the patient
    patient = models.ForeignKey(Patient, null=False, blank=False)
    #orthophoniste = models.ForeignKey(Orthophoniste, null=False, blank=False)
    exercise = models.ForeignKey(Exercise, null=False, blank=False)
    done = models.BooleanField(null=False, blank=True, default=False)

    word = models.CharField(max_length=200, null=True, blank=True)

    def __str__(self):
        return "%s: %s %s" % (self.exercise.name, self.patient.orthophoniste, self.patient)

class Meeting(models.Model):
    patient = models.ForeignKey(Patient, null=False, blank=False)
    time = models.DateTimeField(null=False, blank=False)
    orthophoniste = models.ForeignKey(Orthophoniste, null=False, blank=True)
