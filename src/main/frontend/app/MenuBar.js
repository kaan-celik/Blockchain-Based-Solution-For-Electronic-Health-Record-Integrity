import * as React from "react";
import {AppBar, IconButton, Toolbar} from "@material-ui/core";
import Typography from "@material-ui/core/Typography";
import Button from "@material-ui/core/Button";
import withStyles from "@material-ui/core/styles/withStyles";

const useStyles = () => ({
  root: {
    flexGrow: 1,
  },
  title: {
    flexGrow: 1,
  }
});

class MenuBar extends React.Component {

  constructor(props) {
    super(props);
    this.state = {classes: ''}
  }

  render() {
    const {classes} = this.props;
    return <div className={classes.root}>
      <AppBar position="static" style={{'backgroundColor':'#B80F0A'}}>
        <Toolbar>
          <a href={'/#'} style={{'textDecoration': 'none', 'color':'white', 'display':'contents'}}>
          <Typography align={'center'} variant="h6" className={classes.title}>
            Medical Data Experiment
          </Typography>
          </a>
            <Button color="inherit">Testnet</Button>
        </Toolbar>
      </AppBar>
    </div>
  }

}

export default withStyles(useStyles)(MenuBar);