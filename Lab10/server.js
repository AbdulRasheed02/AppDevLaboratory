const express = require("express")
const path = require("path")
var bodyParser = require('body-parser')

const mongoose = require("mongoose")
mongoose.set('strictQuery', false);

const userModel = require("./userDetails.js");
const app = express();  
var userCredential;

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


app.post('/signup', (req,res)=>{
    console.log("Signup In");
    uname = req.body.uname;
    password = req.body.pass;
    userModel.find({uname: uname})
        .then((foundDetails)=>{
            if(foundDetails.length === 0){
                const details = new userModel({
                    uname: uname,
                    pass: password,
                })
                details.save();
                console.log("New details saved!");
                res.status(201).send({status:"User Registered"});
            }
            else{
                console.log("user already exists!")
                res.status(400).send({status:"User already exists"});
            }
        })
})

app.post('/login', (req,res)=>{
    console.log("Login Endpoint");
    enteredUname = req.body.uname;
    enteredPass = req.body.pass;
    userModel.findOne({uname:enteredUname})
        .then((detail)=>{
            if(detail.pass == enteredPass){
                console.log("Logged in");
                res.status(201).send({status:"Logged In"});
            }
            else{
                console.log("Wrong Credentials!");
                res.status(401).send({status:"Wrong Credentials"});
            }
        })
        .catch((err)=>{
            console.log("User not found!")
            res.status(404).send({status:"User not found!"});
        });

})