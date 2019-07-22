import pygame
import math
import random


pygame.init() 
wScreen = 1200
hScreen = 500
score = 0
adjuster = 5
distX = 0
move = 0
spawn = 0
life = 3
distY = 0
colors = list()
win = pygame.display.set_mode((wScreen,hScreen))
font = pygame.font.Font('freesansbold.ttf', 32) 
resistance = random.randint(-9,9)
text = font.render('Score:' + str(score) , True, (0, 0, 128) , (64,64,64)) 
text = font.render('Life:' + str(life) , True, (0, 0, 128) , (64,64,64)) 
textRect = text.get_rect()  
textRect.center = (50, 80) 

class target(object):
    def __init__(self,x,y,color,length,height):
        self.x = x
        self.y = y
        self.color = color
        self.length = length
        self.height = height
    def draw(self,win):
        #pygame.draw.line(win,self.color,(self.x,self.y+5),(self.x,self.height),3)
        pygame.draw.line(win,self.color,(self.x + spawn + move,self.y+self.height-445),(self.x+self.length + spawn + move,self.y+self.height-445),3)
        #pygame.draw.line(win,self.color,(self.x+self.length,self.y+5),(self.x+self.length,self.height),3)
class ball(object):
    def __init__(self,x,y,radius,color):
        self.x = x
        self.y = y
        self.radius = radius
        self.color = color
    def draw(self,win):
        pygame.draw.circle(win,(0,0,0),(self.x,self.y), self.radius)
        pygame.draw.circle(win,self.color,(self.x,self.y), self.radius-1)
    @staticmethod
    def ballPath(startX,startY,power,ang,time,changeX,changeY):
        velx = math.cos(angle) * power + resistance*5
        vely = math.sin(angle) * power + ((-4.9 * (time)**2)/2)
        
        

        distX =  velx * time 
        distY = (vely * time) + ((-4.9 * (time)**2)/2) 
        newX = round(distX + startX) 
        newY = round(startY - distY) 
        return(newX,newY)
class powergauge(object):
    def __init__(self, x, y, width,height):
        self.x = x
        self.y = y
        self.width = width
        self.height = height
    def draw(self,win):
        pygame.draw.rect(win,(255,255,255),(self.x,self.y,self.width,self.height))

class powerbar(object):
    def __init__(self,x,y,width,height):
        self.x = x
        self.y = y
        self.width = width
        self.height = height
    def draw(self,win):
        pygame.draw.line(win,(0,0,0),(self.x,self.y + self.height/2 +adjuster),(self.x+self.width*1.2,self.y + self.height/2 + adjuster),1)

def redrawWindow():
    win.fill((64,64,64))
    text = font.render('Score: ' + str(score) , True, (0, 0, 128) , (64,64,64))
    wind = font.render('Wind: ' + str(resistance) , True, (0, 0, 128) , (64,64,64))
    life_points = font.render('Life: ' + str(life) , True, (0, 0, 128) , (64,64,64)) 
    textRect2 = wind.get_rect()  
    textRect2.center = (65, 50)  
    rectLife = life_points.get_rect()
    win.blit(life_points,rectLife) 
    win.blit(text,textRect) 
    win.blit(wind,textRect2)
    paperBall.draw(win)
    trash.draw(win)
    #powerMeter.draw(win)
    #bar.draw(win)
    pygame.draw.line(win,(255,255,255), line[0],line[1])
    pygame.display.update()
#def highScore():
    #high_score = read_from_file_and_find_highscore(highScore.txt)

def findAngle(pos):
    sX = paperBall.x
    sY = paperBall.y
    try:
        angle = math.atan((sY - pos[1]) / (sX - pos[0]))
    except:
        angle = math.pi/2
    if pos[1] < sY and pos[0] > sX:
        angle = abs(angle)
    elif pos[1] < sY and pos[0] < sX:
        angle = math.pi - angle
    elif pos[1] > sY and pos[0] < sX:
        angle = math.pi + abs(angle)
    elif pos[1] > sY and pos[0] > sX:
        angle = (math.pi * 2) - angle
    return angle

def checkCollision():
        if paperBall.y > hScreen - paperBall.radius or paperBall.y < paperBall.radius:
            paperBall.ballPath.distY *= -1
        if paperBall.x >wScreen - paperBall.radius or paperBall.x < paperBall.radius:
            paperBall.ballPath.distX *= -1
def targetCollision():
    if paperBall.y > trash.y and paperBall.x == trash.x:
        paperBall.ballPath.changeX *= -1 

paperBall = ball(300,490,7,(255,255,255))
trash = target(800,494,(255,255,0),200,450)
#powerMeter = powergauge(100,200,50,150)
#bar = powerbar(100,200,50,150)
x = 0
y = 0
time = 0
power = 0 
angle = 0
shoot = False
run = True
changeX = 1
changeY = 1
dir = 300
while run:
    if shoot == False:
        #pygame.draw.line(win,(0,0,0),(0,dir),(300,dir),3)
        dir += 1
        if dir == 300 or dir == 350-1:
            dir *= -1
        pygame.display.flip()
    if move == 50:
        move -=1 
    if move >= 0 and move < 50:
        move +=1
    if shoot:
        if paperBall.y < 500 - paperBall.radius:
            time += 0.01
            po = ball.ballPath(x,y,power,angle,time,changeX,changeX)
            paperBall.x = po[0]
            paperBall.y = po[1]
            if paperBall.y > hScreen - paperBall.radius or paperBall.y < paperBall.radius:
                changeX *= 1
            if paperBall.x >wScreen - paperBall.radius or paperBall.x < paperBall.radius:
                changeX *= 1
            if paperBall.y > trash.height and paperBall.y < trash.y and paperBall.x - paperBall.radius >= trash.x:
                changeX *= -1
            if paperBall.y > trash.height and paperBall.y < trash.y and paperBall.x - paperBall.radius >= trash.x + trash.length:
                changeX *= -1
        else:
            shoot = False
            if score > 1:
                spawn = random.randint(-50,50)
            if paperBall.x > trash.x + spawn + move and paperBall.x < trash.x + trash.length + spawn + move:
                score+=1
            else:
                life -= 1
                if life <= 0:
                    score = 0
                    life = 3
            pygame.time.wait(2000)
            resistance = random.randint(-9,9)
            paperBall.y = 490
            paperBall.x = 300
            

    pos = pygame.mouse.get_pos()
    line = [(paperBall.x,paperBall.y),pos]
    redrawWindow()
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
                run = False
        if event.type == pygame.KEYDOWN:
            if event.key == ord("q") and shoot == False:
                adjuster = 0
                shoot = True
                x = paperBall.x
                y = paperBall.y
                time = 0
                power = math.sqrt((line[1][1] - line[0][1])**2 + (line[1][0]-line[0][0])**2)/5
                angle = findAngle(pos)
                count = 0
pygame.quit()
