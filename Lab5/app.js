const express = require("express")
const path = require("path")
var bodyParser = require('body-parser')

const mongoose = require("mongoose")
mongoose.set('strictQuery', false);

const userModel = require("./userDetails.js");

const app = express();  

app.set('views');
app.set('view engine', 'ejs');

app.use(express.json());
app.use(express.urlencoded({ extended: true }));


const dbURI = "mongodb+srv://abdulmdali:fEa0a4XQsdD7sWdO@appdevlab.dcrp1uk.mongodb.net/?retryWrites=true&w=majority";

mongoose.connect(dbURI, {useNewUrlParser : true, useUnifiedTopology : true})
    .then((result)=>{
        console.log("connected to DB");
        app.listen(3000);
    })
    .catch((err)=>{
         console.log("Error connecting to DB");
    });

app.get('/', (req,res)=>{
    res.sendFile(path.join(__dirname, '/home.html'));
})

app.post('/signup', (req,res)=>{
    uname = req.body.uname;
    password = req.body.pswd;
    age = req.body.age;
    email=req.body.email;
    userModel.find({email: email})
        .then((foundDetails)=>{
            if(foundDetails.length === 0){
                const details = new userModel({
                    uname: uname,
                    pass: password,
                    age: age,
                    email: email
                })
                details.save();
                console.log("New details saved!");
                res.redirect('/')
            }
            else{
                console.log("user already exists!")
                res.send("User already exists!");
            }
        })
})

app.post('/login', (req,res)=>{
    enteredEmail = req.body.email;
    enteredPass = req.body.pswd;
    userModel.findOne({email: enteredEmail})
        .then((detail)=>{
            if(detail.pass == enteredPass){
                //res.send({"Username":detail.uname,"Age":detail.age,"Email":detail.email});
                res.send("Username: "+detail.uname+"<br>"+"Age: "+detail.age+"<br>"+"Email: "+detail.email);
            }
            else{
                console.log("Wrong Details!");
                res.send("Wrong Details!");
            }
        })
        .catch((err)=>{
            console.log("User not found!")
            res.send("User not found!");
        });
})