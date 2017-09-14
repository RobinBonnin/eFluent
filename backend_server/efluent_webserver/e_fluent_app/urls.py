from django.conf.urls import include, url
from rest_framework.authtoken import views


from django.contrib.auth.models import User
from e_fluent_app.models import CustomUser
from rest_framework import routers, serializers, viewsets

from views import *

# # Serializers define the API representation.
# class UserSerializer(serializers.HyperlinkedModelSerializer):
#     role = serializers.SerializerMethodField()

#     class Meta:
#         model = CustomUser
#         fields = ('url', 'username', 'email', 
#             'role'
#             )

#     def get_role(self, obj):
#         return str(obj.get_role())
#         #return 'hola'

# # ViewSets define the view behavior.
# class UserViewSet(viewsets.ModelViewSet):
#     queryset = CustomUser.objects.all()
#     serializer_class = UserSerializer

# # Routers provide an easy way of automatically determining the URL conf.
router = routers.DefaultRouter()
# router.register(r'users', UserViewSet)


urlpatterns = [
	url(r'^', include(router.urls)),
    #url(r'^api-token-auth/', views.obtain_auth_token),
    url(r'^register_orthophoniste/', registerOrthiphiniste),
    url(r'^api-token-auth/', CustomLoginHadshake.as_view()),
    url(r'^add_patient/', AddPatient.as_view()),
    url(r'^patient_list/', PatientList.as_view()),
    url(r'^create_meeting/', CreateMeeting.as_view()),
    url(r'^exercises/(?P<pk>[0-9]+)/', Exercises.as_view()),
    url(r'^exercises/', Exercises.as_view()),
    url(r'^makeexercise/(?P<pk>[0-9]+)/$', MakeExercises.as_view()),
    #url(r'^give_exercise/ReconnaissanceVocale/(?P<world>.+)/(?P<pk>[0-9]+)/$', MakeExercises.as_view()),
    url(r'^give_exercise/$', GiveExercises.as_view()),
    #url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework'))
]
