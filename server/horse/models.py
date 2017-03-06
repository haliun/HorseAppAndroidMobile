from django.db import models
# Create your models here.
class User(models.Model):
    lastname=models.CharField(max_length=30)
    firstname=models.CharField(max_length=30)
    email=models.EmailField()
    city=models.CharField(max_length=25)
    district=models.CharField(max_length=25)
    img_user=models.ImageField(upload_to='/images/users/')
###########Основная таблица для коней##################
class Base_horse(models.Model):
    user=models.ForeignKey(User,on_delete=models.CASCADE)
    name=models.CharField(max_length=30)
    private_id=models.IntegerField(max_length=20)
    poroda=models.CharField(max_length=30)
    date_birth=models.DateField()
    age=models.IntegerField(max_length=5)
    place_birth=models.CharField(max_length=30)
    blood=models.CharField(max_length=30)
    color=models.CharField(max_length=30)
    img_horse=models.ImageField(upload_to='/images/horses/')
    img_marking=models.ImageField(upload_to='/images/marking/')
    olymp=models.ForeignKey(Olymp,on_delete=models.CASCADE)

    class Meta:
        abstract=True
########Жеребец############################
class Stallion(Base_horse):
    father=models.ForeignKey('self', on_delete=models.CASCADE)
    mother=models.ForeignKey(FemaleHorse,on_delete=models.CASCADE)
##############Мерин#########
class MaleHorse(Base_horse):
    group=models.ForeignKey(Stallion,on_delete=models.CASCADE)
    mother=models.ForeignKey(FemaleHorse,on_delete=models.CASCADE)
###############Кобыла#################
class FemaleHorse(Base_horse):
    group=models.ForeignKey(Stallion,on_delete=models.CASCADE)
    mother = models.ForeignKey('self', on_delete=models.CASCADE)
#############Жеребенок#################
class Colt(Base_horse):
    gender=models.CharField(max_length=20)
    group=models.ForeignKey(Stallion,on_delete=models.CASCADE)
    mother = models.ForeignKey(FemaleHorse, on_delete=models.CASCADE)

#############Работы#######################
class Works(models.Model):
    name=models.CharField(max_length=50)
    description=models.TextField(max_length=2000)
    edited_time=models.DateTimeField(auto_now_add=True)
    status=models.BooleanField(default=False)
#########Расходы и доходы##############3
class Money(models.Model):
    name=models.CharField(max_length=100)
    type=models.CharField(max_length=20)
    description=models.TextField(max_length=2000)
    date_add=models.DateTimeField(auto_now_add=True)
    sum=models.DecimalField(max_length=10,decimal_places=2)
########Корма и питания####################3
class Food_vit(models.Model):
    name=models.CharField(max_length=40)
    type=models.CharField(max_length=20)
    quantity=models.DecimalField(max_length=6,decimal_places=1)
    sum=models.DecimalField(max_length=10,decimal_places=2)
    description=models.TextField(max_length=2000)
############Для помоши#########################
class Help(models.Model):
    question=models.TextField(max_length=200)
    answer=models.TextField(max_length=2000)
########Таблица для соревнования и конкурса, будет 3 типа, соревнование, малыий конкурс, большой конкурс########
class Olymp(models.Model):
    name=models.CharField(max_length=100)
    type=models.CharField(max_length=50)
    date=models.DateField(auto_now=True)
    place=models.CharField(max_length=200)
    category=models.CharField(max_length=60)
    number_horses=models.IntegerField(max_length=100)
    description=models.TextField(max_length=2000)
    prize=models.ForeignKey(Prize,on_delete=models.CASCADE)
#########Таблица для приза соревнований####################
class Prize(models.Model):
    name=models.CharField(max_length=100)
    description=models.TextField(max_length=1000)


#######Конюшня#######################################
class Stable(models.Model):
    place=models.CharField(max_length=100)
    number_horse=models.IntegerField()
    status=models.CharField(max_length=100)
    season=models.CharField(max_length=20)
    number_personalities=models.IntegerField()

class Levada(models.Model):
    place = models.CharField(max_length=100)
    area = models.FloatField()
    season=models.CharField(max_length=20)
    number_personalities=models.IntegerField()
    grass=models.IntegerField
    cloud=models.IntegerField
    sun=models.IntegerField
