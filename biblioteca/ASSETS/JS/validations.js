const validateYear = year => {
    const MIN_YEAR = 1500;
    const MAX_YEAR = 2023;
  
    if (!(/^[0-9]*$/.test(year))) {
      return false;
    } else {
      if (year < MIN_YEAR || year > MAX_YEAR) {
        return false;
      } else {
        return true;
      }
    }
  }