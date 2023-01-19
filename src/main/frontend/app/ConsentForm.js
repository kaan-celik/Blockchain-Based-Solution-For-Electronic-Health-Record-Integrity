import React from "react";
import Card from "@material-ui/core/Card";
import CardContent from "@material-ui/core/CardContent";
import Typography from "@material-ui/core/Typography";
import Button from "@material-ui/core/Button";
import {green, grey, red, yellow, blue} from "@material-ui/core/colors";
import withStyles from "@material-ui/core/styles/withStyles";
import {CardActions} from "@material-ui/core";
import {withRouter} from "react-router-dom";
import MenuBar from "./MenuBar";
import TextField from "@material-ui/core/TextField";
import Disclaimer from "./Disclaimer";
import {Alert} from "@material-ui/lab";

const useStyles = () => ({
    root: {
        minWidth: 275,
        position: 'absolute', left: '50%', top: '30%',
        transform: 'translate(-50%, -50%)'
    },
    size: {
        margin: '15px',
        display: 'flex'
    },
    bullet: {
        display: 'inline-block',
        margin: '0 2px',
        transform: 'scale(0.8)',
    },
    title: {
        marginBottom: '25px',
    },
    status: {
        marginBottom: 12,
        display: 'inline-block',
        verticalAlign: 'super',
        marginLeft: 6

    },
    greenIcon: {
        display: 'inline-block',
        color: green[500]
    },
    redIcon: {
        display: 'inline-block',
        color: red[500]
    },
    yellowIcon: {
        display: 'inline-block',
        color: yellow[500]
    },
    greyIcon: {
        display: 'inline-block',
        color: grey[500]
    }
});



class ConsentForm extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            classes: '',
            appId: this.props.location.pathname.replace("/consent/", ""),
            mnemonic : '',
            errorMnemonickey: false,
            alert: {display: 'none', text: '', severity: ''},
            linearBarDisplay: 'none',
        }
        this.handleMnemonicKeyChange = this.handleMnemonicKeyChange.bind(this);
        this.routeChange = this.routeChange.bind(this);
        this.confirmation = this.confirmation.bind(this);
        this.deny = this.deny.bind(this);
    }


    validateMnemonicKey() {
        if (this.state.mnemonic === '' || this.state.mnemonic=== 'error' || this.state.mnemonic === undefined) {
            this.setState({errorMnemonickey: true})
            return false
        } else {
            return true
        }
    }

    handleMnemonicKeyChange(event) {
        this.setState({mnemonic: event.target.value})
        console.log(this.state.mnemonic)
    }

    routeChange() {
        this.setState({
            alert: {
                display: 'flex',
                text: 'Success! Wait some seconds for page reloading',
                severity: 'success'
            }})
        setTimeout(function () {
            window.location.reload();
        }.bind(this), 2000);
        let path = '/survey/'+this.state.appId;
        this.props.history.push(path);
    }

    confirmation(){
        console.log(this.state.appId)
        if(this.validateMnemonicKey()){
            fetch("/consent/survey/" + this.state.appId,
                {
                    method: 'POST',
                    headers: {
                        "Accept": "application/json",
                        "Content-Type": "application/json"
                    },
                    body:JSON.stringify(this.state.mnemonic)
                }).then((response) => {
                this.setState({linearBarDisplay: 'none'})
                if(response.ok) this.routeChange();
                else if(response.status == 403) {
                    this.setState({
                        alert: {
                            display: 'flex',
                            text: 'Bu uygulama için yetkiniz bulunmamaktadır. Lütfen yönetici ile iletişime geçiniz.',
                            severity: 'error'
                        }
                    })
                }
                else{
                    this.setState({
                        alert: {
                            display: 'flex',
                            text: 'Something goes wrong! ' + response.status,
                            severity: 'error'
                        }
                    })
                    throw new Error(response.status);
                }
            }).catch((error) => {
                console.log('error: ' + error);
            });
        }
    }

    deny(){

        let path = '/';
        this.props.history.push(path);
    }


    render() {
        const {classes} = this.props;
        return (
            <div>
                <MenuBar/>
                <br/>
                <Card className={classes.root} variant="outlined"
                      style={{'margin': '15px', 'height': 'auto', 'width': 'auto'}}>
                    <CardContent>
                        <Typography variant="h4" gutterBottom className={classes.title}>
                            Onay Formu
                        </Typography>
                        <Typography variant="body2" component="p">
                            Belirtilmiş olan çalışmasına, kendi rızam ile katıldığımı, cevaplarını kendim doldurduğumu ve sonuçları
                            ile ilgili doktorumla paylaşacağım bilgilerin gerektiği durumlarda, heyet ile paylaşılabileceğine izin veriririm. Bunlar dışındaki sistemsel bir
                            sıkıntıdan kaynaklanan herhangi bir durumda sorumluluk tarafıma ait değildir.
                        </Typography>
                        <TextField id="mnemonicKey" label="Parola"
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
                        <Alert style={{display: this.state.alert.display}}
                               severity={this.state.alert.severity}>{this.state.alert.text}</Alert>
                    </CardContent>
                    <CardActions>
                        <Button size="medium" style = {{'backgroundColor':'#005A9C', 'color': 'white'}} onClick={this.confirmation}>Okudum,Onaylıyorum </Button>
                        <Button size="medium" style = {{'backgroundColor':'#B80F0A', 'color': 'white'}} onClick={this.deny}>Reddediyorum  </Button>
                    </CardActions>
                </Card>

            </div>
        );
    }
}

export default withStyles(useStyles)(ConsentForm);