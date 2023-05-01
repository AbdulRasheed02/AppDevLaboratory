const path = require("path");
const Koa = require("koa");
const serve = require("koa-static");
const Router = require("@koa/router");
const multer = require("@koa/multer");
const cors = require("@koa/cors");
const fs = require("fs");

const app = new Koa();
const router = new Router();
const PORT = 3001;

const UPLOAD_DIR = path.join(__dirname, "/uploadFiles");

const express = require("express")
var bodyParser = require('body-parser')

const mongoose = require("mongoose")
mongoose.set('strictQuery', false);

const fileModel = require("./fileDetails.js");

const app2 = express();  

app2.use(express.json());
app2.use(express.urlencoded({ extended: true }));

const dbURI = "mongodb+srv://abdulmdali:fEa0a4XQsdD7sWdO@appdevlab.dcrp1uk.mongodb.net/?retryWrites=true&w=majority";

mongoose.connect(dbURI, {useNewUrlParser : true, useUnifiedTopology : true})
    .then((result)=>{
        console.log("connected to DB");
        app2.listen(3002);
    })
    .catch((err)=>{
         console.log("Error connecting to DB");
    });

const storage = multer.diskStorage({
  destination: function (req, file, cb) {
    cb(null, UPLOAD_DIR);
  },
  filename: function (req, file, cb) {
    cb(null, `${file.originalname}`);
  },
});
const upload = multer({ storage: storage });

router.get("/", async (ctx) => {
  ctx.body = "Hello friends!";
});

function getCookie(name) {
  // Split cookie string and get all individual name=value pairs in an array
  var cookieArr = document.cookie.split(";");
  
  // Loop through the array elements
  for(var i = 0; i < cookieArr.length; i++) {
      var cookiePair = cookieArr[i].split("=");
      
      /* Removing whitespace at the beginning of the cookie name
      and compare it with the given string */
      if(name == cookiePair[0].trim()) {
          // Decode the cookie value and return
          return decodeURIComponent(cookiePair[1]);
      }
  }
  
  // Return null if not found
  return null;
}

// add a route for uploading single files
router.post("/upload-single-file", upload.single("file"), (ctx) => {
  ctx.body = {
    message: `file ${ctx.request.file.filename} has saved on the server`,
    url: `http://localhost:${PORT}/${ctx.request.file.originalname}`,
  };
  var userCredential=app.userCredential;
  console.log("Hi"+userCredential);
  const details = new fileModel({
    email: userCredential ,
    uri: `http://localhost:${PORT}/${ctx.request.file.originalname}`,
  })
  details.save();
  console.log("New details saved!");

});

app.use(cors());
app.use(router.routes()).use(router.allowedMethods());
app.use(serve(UPLOAD_DIR));

app.listen(PORT, () => {
  console.log(`app starting at port ${PORT}`);
});
