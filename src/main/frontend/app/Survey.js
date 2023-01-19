import * as React from "react";
import Grid from "@material-ui/core/Grid";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import RadioGroup from "@material-ui/core/RadioGroup";
import FormControl from "@material-ui/core/FormControl";
import Radio from "@material-ui/core/Radio";
import Button from "@material-ui/core/Button";
import Brightness1Icon from '@material-ui/icons/Brightness1';
import {green, grey, red, yellow} from '@material-ui/core/colors';
import TextField from "@material-ui/core/TextField";
import withStyles from "@material-ui/core/styles/withStyles";
import Divider from "@material-ui/core/Divider";
import {getIconColor} from "./SurveyItem";
import MenuBar from "./MenuBar";
import {Alert} from "@material-ui/lab";
import {CircularProgress, LinearProgress} from "@material-ui/core";
import Disclaimer from "./Disclaimer";

const useStyles = () => ({
  spaces: {
    margin: '15px'
  },
  question: {
    marginTop: '-17px',
    marginLeft: '20px'
  },
  size: {
    margin: '15px',
    display: 'flex'
  },
  verticalBar: {
    display: 'inline-block'
  },
  greenIcon: {
    margin: '15px',
    verticalAlign: 'top',
    position: 'absolute',
    color: green[500]
  },
  redIcon: {
    margin: '15px',
    verticalAlign: 'top',
    position: 'absolute',
    color: red[500]
  },
  yellowIcon: {
    margin: '15px',
    verticalAlign: 'top',
    position: 'absolute',
    color: yellow[500]
  },
  greyIcon: {
    margin: '15px',
    verticalAlign: 'top',
    position: 'absolute',
    color: grey[500]
  },
  circularProgress: {
    display: 'flex',
    // '& > * + *': {
    //   marginLeft: '2px',
    // },
    position: 'absolute',
    top: '34%',
    left: '50%',
    marginTop: '-50px',
    marginLeft: '-50px',
  }
});

export class Survey extends React.Component {

  constructor(props, context) {
    super(props, context);
    this.state = {
      appId: this.props.location.pathname.replace("/survey/", ""), //  56701999,
      selectedOption: '',
      survey: '',
      mnemonic : '',
      errorMnemonickey: false,
      alert: {display: 'none', text: '', severity: ''},
      linearBarDisplay: 'none',
      surveyStatus: 'Open'
    }


    this.handleChange = this.handleChange.bind(this);
    this.handleMnemonicKeyChange = this.handleMnemonicKeyChange.bind(this);
    this.submitVote = this.submitVote.bind(this);
    this.getSurvey = this.getSurvey.bind(this);
  }

  componentDidMount() {
    this.getSurvey()
  }



  getSurvey() {

    fetch("/surveys/" + this.state.appId)
        .then(function (response) {
          if (response.ok) {
            response.json().then(function (data) {
              console.log(data)
              this.setState({
                survey: data
              });
            }.bind(this));
          } else {
            window.location.reload();
          }
        }.bind(this));
  }

  submitVote() {
    console.log(this.state.appId)
    this.state.survey.password = this.state.mnemonic
    this.setState({linearBarDisplay: 'flex'})
    console.log(JSON.stringify(this.state.survey))
    if (this.validateParams()) {
      fetch("/fill/survey/" + this.state.appId,
          {
            method: 'POST',
            headers: {
              "Accept": "application/json",
              "Content-Type": "application/json"
            },
            body: JSON.stringify(this.state.survey)
          }).then(this.handleResponse());
    }else{
      this.setState({linearBarDisplay: 'none'})
    }
  }

  handleResponse() {
    return (response) => {
      this.setState({linearBarDisplay: 'none'})
      if (response.ok) {
        this.setState({
          alert: {
            display: 'flex',
            text: 'Success! Wait some seconds for page reloading',
            severity: 'success'
          }
        })
        setTimeout(function () {
          window.location.reload();
        }.bind(this), 9998)
      } else {
        response.text().then(message => {
          this.setState({
            alert: {
              display: 'flex',
              text: 'Something goes wrong! ' + message,
              severity: 'error'
            }
          })
        })
      }
    };
  }

  validateParams() {
    return this.validateMnemonicKey();
  }

  validateMnemonicKey() {
    if (this.state.survey.password === '' || this.state.survey.password === 'error' || this.state.survey.password === undefined) {
      console.log(this.state.survey)
      this.setState({errorMnemonickey: true})
      return false
    } else {
      return true
    }
  }

  handleChange(event) {

    this.state.survey.questionList[event.target.name].questionOptionList.forEach((opt,index) => {
          if (opt.option === event.target.value) {
            opt.isselected = true;
          } else {
            opt.isselected = false;
          }
        }
    );

    console.log(this.state.survey);
    console.log(event.target.value);

   };

  handleMnemonicKeyChange(event) {
    this.setState({mnemonic: event.target.value})
    console.log(this.state.mnemonic)
  }

  render() {
    const {classes} = this.props;
    return <div>
      <MenuBar/>
      <br/>
      {this.state.survey === '' ?
          <div className={classes.circularProgress} >
            <CircularProgress size={100}/>
          </div> :

          <Grid container>
            <Grid item xs={3}/>
            <Grid item xs={6}>
              <Paper>
                <Grid container>
                  <Grid item xs={7}>
                    <Typography variant="h4" component="h2"
                                className={classes.spaces}>
                      {this.state.survey.name}
                    </Typography>
                  </Grid>
                  <Grid item xs={7}>
                    <Divider/>
                    <Typography variant="body1" className={classes.spaces}>
                      {this.state.survey.description}
                    </Typography>
                    <br/>
                  </Grid>
                  <Grid item xs={1}>
                    <Divider/>
                    <Divider orientation={'vertical'}
                             className={classes.verticalBar}/>
                    <Brightness1Icon
                        className={this.state.survey ? getIconColor.call(this,
                            classes, this.state.surveyStatus) : ''}/>
                  </Grid>
                  <Grid item xs={4}>
                    <Divider/>
                    <div style={{textAlign: 'left'}}>
                      <Typography variant='overline' className={classes.spaces}>
                        Bu anket {this.state.surveyStatus}
                      </Typography>
                    </div>
                  </Grid>
                </Grid>
                <Divider/>
                <FormControl component="fieldset" className={classes.size}>

                  {this.state.survey.questionList.map((item,index) =>
                       <div key={index}>
                         <Typography variant='overline' className={classes.spaces}>
                           {item.question}
                         </Typography>
                         <br></br>
                         <RadioGroup aria-label="gender" name={index} >
                         {item.questionOptionList.map((opt,Index) =>
                             <FormControlLabel
                                 value={opt.option}
                                 control={<Radio/>}
                                 label={opt.option}
                                 onChange={this.handleChange}
                             />
                         )}
                         </RadioGroup>
                       </div>
                )}



                  <TextField id="mnemonicKey" label="Password"
                             error={this.state.errorMnemonickey}
                             multiline
                             rows={4}
                             defaultValue="Default Value"
                             variant="outlined"
                             value={this.state.mnemonic}
                             onChange={this.handleMnemonicKeyChange}
                             className={classes.size}
                  />
                  <Disclaimer/>
                </FormControl>
                <Alert style={{display: this.state.alert.display}}
                       severity={this.state.alert.severity}>{this.state.alert.text}</Alert>
                <LinearProgress style={{display: this.state.linearBarDisplay}}/>
                <div style={{textAlign: 'center'}}>
                  <Button onClick={this.submitVote} variant="contained"
                          color="primary"
                          className={classes.spaces}>Vote</Button>
                </div>
              </Paper>
            </Grid>
            <Grid item xs={3}/>
          </Grid>
      }
    </div>
  }
}

export default withStyles(useStyles)(Survey);