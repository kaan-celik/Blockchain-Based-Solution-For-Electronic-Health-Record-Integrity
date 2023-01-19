import GetSurveys from "./GetSurveys";
import React from "react";
import Grid from "@material-ui/core/Grid";
import Typography from "@material-ui/core/Typography";
import MenuBar from "./MenuBar";


class Home extends React.Component {


  render() {
    return <div>
      <MenuBar/>
      <Grid container>
        <Grid item xs={2}/>
        <Grid item xs={8}>
          <Typography align={'center'} style={{marginTop: '15px'}} variant="h2">You can complete the given survey</Typography>
          <Typography align={'center'} style={{marginTop: '5px'}} variant="h5">
          </Typography>
          <Typography align={'center'} variant="h5">The security is provided by Algorand Blockchain</Typography>
        </Grid>
        <Grid item xs={2}/>
      </Grid>
      <br/>

      <Grid container>
        <Grid xs={3}/>
        <Grid xs={6}>
          <Grid container>
            <GetSurveys/>
          </Grid>
        </Grid>
        <Grid xs={3}/>
      </Grid>
    </div>
  }

}

export default Home;

