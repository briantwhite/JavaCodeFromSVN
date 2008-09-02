import appuifw, positioning, e32, camera, key_codes, time

pos = None

state = "welcome"

def redraw(rect):
    global state
    w, h = canvas.size
    if (state == 'welcome'):
        canvas.rectangle((0,0,w,h), fill=(255,255,0))
        canvas.text((20,20), u"Welcome to the GPSCam", fill = (0,0,0))
        canvas.text((20,40), u"Get your position and take a picture", fill = (0,0,0))
    elif (state == 'get_current_pos'):
        canvas.rectangle((0,0,w,h), fill=(0,0,255))
        canvas.text((20,20), u"Getting position", fill = (0,0,0))
        canvas.text((20,40), u"This can take a long time", fill = (0,0,0))
        canvas.text((20,60), u"So please be patient.", fill = (0,0,0))
        canvas.text((20,80), u"DO NOT TRY TO QUIT!", fill = (255,255,0))
    elif (state == 'have_position'):
        canvas.rectangle((0,0,w,h), fill=(255,0,255))
        canvas.text((20,20), u"Have position; take picture", fill = (0,0,0))       
    elif (state == 'take_picture'):
        canvas.rectangle((0,0,w,h), fill=(0,255,0))
        canvas.text((20,20), u"Please wait for the viewfinder", fill = (0,0,0))
        canvas.text((20,40), u"to appear in this space.", fill = (0,0,0))
        canvas.text((20,h - 40), u"Press the center button", fill = (0,0,0))
        canvas.text((20,h - 20), u"to take a picture.", fill = (0,0,0))
 
    else:
        canvas.rectangle((0,0,w,h), fill=(0,0,0))
        canvas.text((20,20), u"Where am I?", fill = (255,255,255))
        
def quit():
    app_lock.signal()

def get_current_position():
    global pos
    global state
    state = "get_current_pos"
    long_pos = positioning.position()
    pos = long_pos['position']
    pos_string = u"Lat: " + str(pos['latitude']) + "\n" + u"Long: " + str(pos['longitude'])
    appuifw.query(pos_string, "query")
    state = "have_position"
    
def get_last_position():
    global pos
    global state
    pos = positioning.last_position()
    pos_string = u"Lat: " + str(pos['latitude']) + "\n" + u"Long: " + str(pos['longitude'])
    appuifw.query(pos_string, "query")
    state = "have_position"

def viewfinder(img):
    canvas.blit(img)
    
def shoot():
    global state
    camera.stop_finder()
    pic_time = time.ctime()
    pos_string = u"Lat: " + str(pos['latitude']) + u"      Long: " + str(pos['longitude'])
    pos_string = pos_string + "      " + pic_time
    photo = camera.take_photo(size = (640, 480))
    w, h = photo.size
    photo.rectangle((0, 0, w, 15), fill = (255,0,255))
    photo.text((5,12), pos_string, fill = (255, 255, 255))
    
    w, h = canvas.size
    canvas.rectangle((0,0,w,h), fill=(255,255,255))
    canvas.text((20,h - 20), u"Picture ready", fill = (0,0,0))
    canvas.blit(photo, target = (0, 0, w, 0.75 * w), scale = 1)
    pic_time = pic_time.replace(":","-")
    filename = "E:\\Images\\" + pic_time.replace(" ", "_") + ".jpg"
    photo.save(filename)
    
def take_picture():
    global pos
    global state
    state = "take_picture"
    if not pos:
        appuifw.query(u"You must get your position before taking a picture", "query")
    else:
        camera.start_finder(viewfinder)

    
appuifw.app.exit_key_handler = quit
appuifw.app.title = u"GPS Camera"

canvas = appuifw.Canvas(redraw_callback = redraw)
appuifw.app.body = canvas
canvas.bind(key_codes.EKeySelect, shoot)
appuifw.app.screen = "normal"
appuifw.app.menu = [(u"Get Current Position", get_current_position), 
                    (u"Get Last Position", get_last_position), 
                    (u"Take Photo", take_picture)]

positioning.select_module(positioning.default_module())
positioning.set_requestors([{"type":"service", "format":"application", "data":"test_app"}]) 

app_lock = e32.Ao_lock()

app_lock.wait()
